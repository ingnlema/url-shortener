package com.mercadolibre.urlshortener.service;

import com.mercadolibre.urlshortener.exception.UrlNotFoundException;
import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.repository.UrlMappingRepository;
import com.mercadolibre.urlshortener.util.Base62Encoder;
import com.mercadolibre.urlshortener.util.UniqueIdGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlShorteningServiceTest {

    @Mock
    private UrlMappingRepository repository;

    @Mock
    private UniqueIdGenerator idGenerator;

    @InjectMocks
    private UrlShorteningService service;

    @Test
    void testCreateShortUrl() {
        String originalUrl = "http://example.com";
        long uniqueId = 1L;
        String encodedId = Base62Encoder.encode(uniqueId);
        UrlMapping urlMapping = new UrlMapping(encodedId, originalUrl);

        when(idGenerator.generateUniqueId()).thenReturn(uniqueId);
        when(repository.save(any(UrlMapping.class))).thenReturn(urlMapping);

        UrlMapping result = service.createShortUrl(originalUrl);

        assertEquals(encodedId, result.getId());
        assertEquals(originalUrl, result.getOriginalUrl());
    }

    @Test
    void testGetOriginalUrl() {
        String id = "abc123";
        String originalUrl = "http://example.com";
        UrlMapping urlMapping = new UrlMapping(id, originalUrl);

        when(repository.findById(id)).thenReturn(Optional.of(urlMapping));

        String result = service.getOriginalUrl(id);

        assertEquals(originalUrl, result);
    }

    @Test
    void testGetOriginalUrlThrowsException() {
        String id = "abc123";

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> {
            service.getOriginalUrl(id);
        });
    }
}
