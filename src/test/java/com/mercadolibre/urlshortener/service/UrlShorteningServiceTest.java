package com.mercadolibre.urlshortener.service;

import com.mercadolibre.urlshortener.config.CacheConfig;
import com.mercadolibre.urlshortener.exception.UrlNotFoundException;
import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.repository.UrlMappingRepository;
import com.mercadolibre.urlshortener.util.UniqueIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CacheConfig.class})
public class UrlShorteningServiceTest {

    @Mock
    private UrlMappingRepository repository;

    @Mock
    private UniqueIdGenerator idGenerator;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private UrlShorteningService urlShorteningService;

    private Cache cache;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cache = new ConcurrentMapCache("urlCache");
        when(cacheManager.getCache("urlCache")).thenReturn(cache);
    }

    @Test
    public void testCreateShortUrl() {
        String originalUrl = "https://example.com";
        long uniqueId = 123L;
        String encodedId = "abc123";

        when(idGenerator.generateUniqueId()).thenReturn(uniqueId);
        when(repository.save(any(UrlMapping.class))).thenReturn(new UrlMapping(encodedId, originalUrl));

        UrlMapping result = urlShorteningService.createShortUrl(originalUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(encodedId, result.getId());
    }

    @Test
    void testGetOriginalUrl_Success() {
        String shortUrl = "abc123";
        String originalUrl = "http://example.com";
        UrlMapping urlMapping = new UrlMapping(shortUrl, originalUrl);

        when(repository.findById(shortUrl)).thenReturn(Optional.of(urlMapping));

        String result = urlShorteningService.getOriginalUrl(shortUrl);

        assertEquals(originalUrl, result);
        verify(repository, times(2)).findById(shortUrl);
    }

    @Test
    void testGetOriginalUrl_NotFound() {
        String shortUrl = "abc123";

        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> {
            urlShorteningService.getOriginalUrl(shortUrl);
        });

        verify(repository, times(1)).findById(shortUrl);
    }


    @Test
    void testGetOriginalUrlFromCache_CacheMiss() {
        String shortUrl = "abc123";
        String originalUrl = "http://example.com";
        UrlMapping urlMapping = new UrlMapping(shortUrl, originalUrl);

        when(repository.findById(shortUrl)).thenReturn(Optional.of(urlMapping));

        String result = urlShorteningService.getOriginalUrlFromCache(shortUrl);

        assertEquals(originalUrl, result);
        verify(repository, times(1)).findById(shortUrl); // Should hit the repository
    }

    @Test
    void testGetOriginalUrlFromCache_NotFound() {
        String shortUrl = "abc123";

        when(repository.findById(shortUrl)).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> {
            urlShorteningService.getOriginalUrlFromCache(shortUrl);
        });

        verify(repository, times(1)).findById(shortUrl);
    }

    @Test
    void testUpdateUrlAccessStats() {
        String shortUrl = "abc123";
        UrlMapping urlMapping = new UrlMapping(shortUrl, "http://example.com");
        urlMapping.setAccessCount(0);
        urlMapping.setFirstAccess(null);
        urlMapping.setLastAccess(null);

        when(repository.findById(shortUrl)).thenReturn(Optional.of(urlMapping));
        when(repository.save(any(UrlMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        urlShorteningService.updateUrlAccessStats(shortUrl);

        assertEquals(1, urlMapping.getAccessCount());
        assertEquals(urlMapping.getFirstAccess(), urlMapping.getLastAccess());
        verify(repository, times(1)).save(urlMapping);
    }

    @Test
    void testUpdateUrlAccessStats_NotFound() {
        String shortUrl = "abc123";

        when(repository.findById(shortUrl)).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlShorteningService.updateUrlAccessStats(shortUrl));
        verify(repository, times(0)).save(any(UrlMapping.class));
    }

    @Test
    void testGetStats_Success() {
        String shortUrl = "abc123";
        UrlMapping urlMapping = new UrlMapping(shortUrl, "http://example.com");
        urlMapping.setAccessCount(10);

        when(repository.findById(shortUrl)).thenReturn(Optional.of(urlMapping));

        UrlMapping result = urlShorteningService.getStats(shortUrl);

        assertNotNull(result);
        assertEquals(urlMapping.getAccessCount(), result.getAccessCount());
        assertEquals(urlMapping.getOriginalUrl(), result.getOriginalUrl());
        verify(repository, times(1)).findById(shortUrl);
    }

    @Test
    void testGetStats_NotFound() {
        String shortUrl = "abc123";

        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> {
            urlShorteningService.getStats(shortUrl);
        });

        verify(repository, times(1)).findById(shortUrl);
    }
}