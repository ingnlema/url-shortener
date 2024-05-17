package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.model.dto.UrlDto;
import com.mercadolibre.urlshortener.model.dto.PerformanceMeasureDto;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final UrlShorteningService urlShorteningService;

    @Autowired
    public PerformanceController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<PerformanceMeasureDto> measureCreateShortUrl(@RequestBody UrlDto urlDto) {
        long startTime = System.currentTimeMillis();
        try {
            urlShorteningService.createShortUrl(urlDto.getUrl());
            long endTime = System.currentTimeMillis();
            return ResponseEntity.ok(new PerformanceMeasureDto(endTime - startTime, "Creación de URL corta"));
        } catch (Exception e) {
            log.error("Error al medir la creación de URL corta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<PerformanceMeasureDto> measureGetOriginalUrl(@PathVariable String shortUrl) {
        long startTime = System.currentTimeMillis();
        try {
            urlShorteningService.getOriginalUrl(shortUrl);
            long endTime = System.currentTimeMillis();
            return ResponseEntity.ok(new PerformanceMeasureDto(endTime - startTime, "Obtención de URL original"));
        } catch (Exception e) {
            log.error("Error al medir la obtención de URL original: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<PerformanceMeasureDto> measureDeleteShortUrl(@PathVariable String shortUrl) {
        long startTime = System.currentTimeMillis();
        try {
            urlShorteningService.deleteUrl(shortUrl);
            long endTime = System.currentTimeMillis();
            return ResponseEntity.ok(new PerformanceMeasureDto(endTime - startTime, "Eliminación de URL corta"));
        } catch (Exception e) {
            log.error("Error al medir la eliminación de URL corta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/{shortUrl}")
    public ResponseEntity<PerformanceMeasureDto> measureGetUrlStats(@PathVariable String shortUrl) {
        long startTime = System.currentTimeMillis();
        try {
            urlShorteningService.getStats(shortUrl);
            long endTime = System.currentTimeMillis();
            return ResponseEntity.ok(new PerformanceMeasureDto(endTime - startTime, "Estadísticas de URL corta"));
        } catch (Exception e) {
            log.error("Error al medir las estadísticas de la URL corta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
