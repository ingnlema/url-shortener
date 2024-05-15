package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.model.dto.UrlDto;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<UrlMapping> createShortUrl(@RequestBody UrlDto urlDto) {
        UrlMapping urlMapping = urlShorteningService.createShortUrl(urlDto.getUrl());
        return ResponseEntity.ok(urlMapping);
    }

    @Operation(summary = "Obtiene la URL original a partir de una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL original encontrada"),
            @ApiResponse(responseCode = "404", description = "URL no encontrada")
    })
    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlMapping> getOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = urlShorteningService.getOriginalUrl(shortUrl);
        UrlMapping urlMapping = new UrlMapping(shortUrl, originalUrl);
        return ResponseEntity.ok(urlMapping);
    }

    @Operation(summary = "Elimina una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL corta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "URL no encontrada")
    })
    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortUrl) {
        urlShorteningService.deleteUrl(shortUrl);
        return ResponseEntity.ok().build();
    }

}
