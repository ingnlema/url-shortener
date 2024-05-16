package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.model.dto.ShortUrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlStatsDto;
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
    public ResponseEntity<ShortUrlDto> createShortUrl(@RequestBody UrlDto urlDto) {
        UrlMapping urlMapping = urlShorteningService.createShortUrl(urlDto.getUrl());
        ShortUrlDto shortUrlDto = new ShortUrlDto(urlMapping.getId());
        return ResponseEntity.ok(shortUrlDto);
    }

    @Operation(summary = "Obtiene la URL original a partir de una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL original encontrada"),
            @ApiResponse(responseCode = "404", description = "URL no encontrada")
    })
//    @GetMapping("/url/{shortUrl}")
//    public ResponseEntity<UrlDto> getOriginalUrl(@PathVariable String shortUrl) {
//        String originalUrl = urlShorteningService.getOriginalUrl(shortUrl);
//        UrlDto urlDto = new UrlDto(originalUrl);
//        return ResponseEntity.ok(urlDto);
//    }
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
//    @GetMapping("/url/stats/{shortUrl}")
//    public ResponseEntity<UrlStatsDto> getUrlStats(@PathVariable String shortUrl) {
//        UrlMapping urlMapping = urlShorteningService.getStats(shortUrl);
//        UrlStatsDto stats = new UrlStatsDto(
//                urlMapping.getAccessCount(),
//                urlMapping.getFirstAccess(),
//                urlMapping.getLastAccess()
//        );
//        return ResponseEntity.ok(stats);
//    }

}
