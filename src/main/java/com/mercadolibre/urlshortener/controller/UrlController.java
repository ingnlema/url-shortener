package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlShorteningService urlService;

    @PostMapping("/shorten")
    public UrlMapping shortenUrl(@RequestBody String originalUrl) {
        return urlService.createShortUrl(originalUrl);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String id) {
        String originalUrl = urlService.getOriginalUrl(id);
        return ResponseEntity.ok(originalUrl);
    }
}
