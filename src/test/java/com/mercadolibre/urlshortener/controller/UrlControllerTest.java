package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.exception.UrlNotFoundException;
import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.model.dto.ShortUrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlStatsDto;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UrlControllerTest {

    @Mock
    private UrlShorteningService urlShorteningService;

    private MeterRegistry meterRegistry;

    private UrlController urlController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meterRegistry = new SimpleMeterRegistry();
        urlController = new UrlController(urlShorteningService, meterRegistry);
    }

    @Test
    void createShortUrl_Success() {
        UrlDto urlDto = new UrlDto("http://example.com");
        UrlMapping urlMapping = new UrlMapping("shortUrl", "http://example.com");
        when(urlShorteningService.createShortUrl(anyString())).thenReturn(urlMapping);

        ResponseEntity<ShortUrlDto> response = urlController.createShortUrl(urlDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("shortUrl", response.getBody().getShortUrl());
        verify(urlShorteningService, times(1)).createShortUrl(anyString());
    }

    @Test
    void createShortUrl_InternalServerError() {
        UrlDto urlDto = new UrlDto("http://example.com");
        when(urlShorteningService.createShortUrl(anyString())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<ShortUrlDto> response = urlController.createShortUrl(urlDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(urlShorteningService, times(1)).createShortUrl(anyString());
    }

    @Test
    void getOriginalUrl_Success() {
        String shortUrl = "shortUrl";
        when(urlShorteningService.getOriginalUrl(anyString())).thenReturn("http://example.com");

        ResponseEntity<UrlDto> response = urlController.getOriginalUrl(shortUrl);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("http://example.com", response.getBody().getUrl());
        verify(urlShorteningService, times(1)).getOriginalUrl(anyString());
    }

    @Test
    void getOriginalUrl_NotFound() {
        String shortUrl = "shortUrl";
        when(urlShorteningService.getOriginalUrl(anyString())).thenThrow(new UrlNotFoundException("Not Found"));

        ResponseEntity<UrlDto> response = urlController.getOriginalUrl(shortUrl);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(urlShorteningService, times(1)).getOriginalUrl(anyString());
    }

    @Test
    void deleteShortUrl_Success() {
        String shortUrl = "shortUrl";
        doNothing().when(urlShorteningService).deleteUrl(anyString());

        ResponseEntity<Void> response = urlController.deleteShortUrl(shortUrl);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(urlShorteningService, times(1)).deleteUrl(anyString());
    }

    @Test
    void deleteShortUrl_NotFound() {
        String shortUrl = "shortUrl";
        doThrow(new UrlNotFoundException("Not Found")).when(urlShorteningService).deleteUrl(anyString());

        ResponseEntity<Void> response = urlController.deleteShortUrl(shortUrl);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(urlShorteningService, times(1)).deleteUrl(anyString());
    }

    @Test
    void getUrlStats_Success() {
        String shortUrl = "shortUrl";
        UrlMapping urlMapping = new UrlMapping(shortUrl, "http://example.com");
        urlMapping.setAccessCount(10);
        urlMapping.setFirstAccess(LocalDateTime.of(2024, 1, 1, 0, 0));
        urlMapping.setLastAccess(LocalDateTime.of(2024, 5, 17, 0, 0));
        when(urlShorteningService.getStats(anyString())).thenReturn(urlMapping);

        ResponseEntity<UrlStatsDto> response = urlController.getUrlStats(shortUrl);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getAccessCount());
        assertEquals("2024-01-01T00:00:00", response.getBody().getFirstAccess());
        assertEquals("2024-05-17T00:00:00", response.getBody().getLastAccess());
        verify(urlShorteningService, times(1)).getStats(anyString());
    }

    @Test
    void getUrlStats_NotFound() {
        String shortUrl = "shortUrl";
        when(urlShorteningService.getStats(anyString())).thenThrow(new UrlNotFoundException("Not Found"));

        ResponseEntity<UrlStatsDto> response = urlController.getUrlStats(shortUrl);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(urlShorteningService, times(1)).getStats(anyString());
    }
}
