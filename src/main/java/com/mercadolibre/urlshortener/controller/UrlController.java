package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.model.dto.ShortUrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlStatsDto;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Operation(summary = "Crea una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL corta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlDto> createShortUrl(@RequestBody UrlDto urlDto) {
        log.info("Solicitud recibida para crear una URL corta para la URL original: {}", urlDto.getUrl());
        UrlMapping urlMapping = urlShorteningService.createShortUrl(urlDto.getUrl());
        ShortUrlDto shortUrlDto = new ShortUrlDto(urlMapping.getId());
        log.info("URL corta creada exitosamente: {}", shortUrlDto.getShortUrl());
        return ResponseEntity.ok(shortUrlDto);
    }

    @Operation(summary = "Obtiene la URL original a partir de una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL original encontrada"),
            @ApiResponse(responseCode = "404", description = "URL no encontrada")
    })
    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlDto> getOriginalUrl(@PathVariable String shortUrl) {
        log.info("Solicitud recibida para obtener la URL original para la URL corta: {}", shortUrl);
        String originalUrl = urlShorteningService.getOriginalUrl(shortUrl);
        UrlDto urlDto = new UrlDto(originalUrl);
        log.info("URL original encontrada para la URL corta: {}", shortUrl);
        return ResponseEntity.ok(urlDto);
    }
    @Operation(summary = "Elimina una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL corta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "URL no encontrada")
    })
    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortUrl) {
        log.info("Solicitud recibida para eliminar la URL corta: {}", shortUrl);
        urlShorteningService.deleteUrl(shortUrl);
        log.info("URL corta eliminada exitosamente: {}", shortUrl);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats/{shortUrl}")
    public ResponseEntity<UrlStatsDto> getUrlStats(@PathVariable String shortUrl) {
        log.info("Solicitud recibida para obtener estadísticas para la URL corta: {}", shortUrl);
        UrlMapping urlMapping = urlShorteningService.getStats(shortUrl);
        UrlStatsDto stats = new UrlStatsDto(
                urlMapping.getAccessCount(),
                urlMapping.getFirstAccess(),
                urlMapping.getLastAccess()
        );
        log.info("Estadísticas obtenidas para la URL corta: {}", shortUrl);
        return ResponseEntity.ok(stats);
    }

}
