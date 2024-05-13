package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.model.dto.UrlDto;
import com.mercadolibre.urlshortener.resource.UrlResource;
import com.mercadolibre.urlshortener.resource.UrlResourceAssembler;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/url")
@Slf4j
public class UrlController {

    private final UrlResourceAssembler assembler;
    private final UrlShorteningService urlShorteningService;

    @Autowired
    public UrlController(UrlShorteningService urlShorteningService, UrlResourceAssembler assembler) {
        this.urlShorteningService = urlShorteningService;
        this.assembler = assembler;
    }

    @Operation(summary = "Crea una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL corta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/shorten")
    public ResponseEntity<UrlResource> createShortUrl(@RequestBody UrlDto urlDto) {
        UrlMapping urlMapping = urlShorteningService.createShortUrl(urlDto.getUrl());
        UrlResource urlResource = assembler.toModel(urlMapping);
        return ResponseEntity.ok(urlResource);
    }

    @Operation(summary = "Obtiene la URL original a partir de una URL corta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL original encontrada"),
            @ApiResponse(responseCode = "404", description = "URL no encontrada")
    })
    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlResource> getOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = urlShorteningService.getOriginalUrl(shortUrl);
        UrlMapping urlMapping = new UrlMapping(shortUrl, originalUrl);
        UrlResource urlResource = assembler.toModel(urlMapping);
        return ResponseEntity.ok(urlResource);
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

    @Operation(summary = "Obtiene todas las URLs cortas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de URLs cortas obtenida exitosamente"),
    })
    @GetMapping
    public ResponseEntity<CollectionModel<UrlResource>> getAllUrls() {
        List<UrlMapping> urlMappings = urlShorteningService.getAllUrls();
        List<UrlResource> resources = urlMappings.stream().map(assembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(resources));
    }
}
