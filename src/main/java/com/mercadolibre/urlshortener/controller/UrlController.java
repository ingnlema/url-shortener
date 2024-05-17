package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.exception.UrlNotFoundException;
import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.model.dto.ShortUrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlStatsDto;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/url")
public class UrlController {


    private final UrlShorteningService urlShorteningService;


    @Autowired
    public UrlController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;

    }


    @Operation(summary = "Crear una URL acortada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL acortada exitosamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UrlMapping.class)) }),
            @ApiResponse(responseCode = "400", description = "URL inválida proporcionada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content) })
    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlDto> createShortUrl(@RequestBody UrlDto urlDto) {
        try {
            log.info("Solicitud recibida para crear una URL corta para la URL original: {}", urlDto.getUrl());

            UrlMapping urlMapping = urlShorteningService.createShortUrl(urlDto.getUrl());
            ShortUrlDto shortUrlDto = new ShortUrlDto(urlMapping.getId());

            log.info("URL corta creada exitosamente: {}", shortUrlDto.getShortUrl());

            return ResponseEntity.ok(shortUrlDto);

        } catch (Exception e) {
            log.error("Error al crear la URL acortada: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Obtener la URL original a partir de una URL acortada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL original obtenida exitosamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "URL acortada no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content) })
    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlDto> getOriginalUrl(@PathVariable String shortUrl) {
        try {
            log.info("Solicitud recibida para obtener la URL original para la URL corta: {}", shortUrl);

            String originalUrl = urlShorteningService.getOriginalUrl(shortUrl);
            UrlDto urlDto = new UrlDto(originalUrl);

            log.info("URL original encontrada para la URL corta: {}", shortUrl);

            return ResponseEntity.ok(urlDto);

        } catch (UrlNotFoundException e) {
            log.error("URL acortada no encontrada: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error al obtener la URL original: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Eliminar una URL acortada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL acortada eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "URL acortada no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content) })
    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortUrl) {
        try {
            log.info("Solicitud recibida para eliminar la URL corta: {}", shortUrl);

            urlShorteningService.deleteUrl(shortUrl);

            log.info("URL corta eliminada exitosamente: {}", shortUrl);

            return ResponseEntity.ok().build();

        } catch (UrlNotFoundException e) {
            log.error("URL acortada no encontrada: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error al eliminar la URL acortada: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Obtener estadísticas de una URL acortada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UrlStatsDto.class)) }),
            @ApiResponse(responseCode = "404", description = "URL acortada no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content) })
    @GetMapping("/stats/{shortUrl}")
    public ResponseEntity<UrlStatsDto> getUrlStats(@PathVariable String shortUrl) {
        try {
            log.info("Solicitud recibida para obtener estadísticas para la URL corta: {}", shortUrl);

            UrlMapping urlMapping = urlShorteningService.getStats(shortUrl);

            UrlStatsDto stats = new UrlStatsDto(
                    urlMapping.getAccessCount(),
                    urlMapping.getFirstAccess(),
                    urlMapping.getLastAccess()
            );

            log.info("Estadísticas obtenidas para la URL corta: {}", shortUrl);

            return ResponseEntity.ok(stats);
        } catch (UrlNotFoundException e) {
            log.error("URL acortada no encontrada: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error al obtener estadísticas para la URL corta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}
