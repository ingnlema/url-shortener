package com.mercadolibre.urlshortener.service;

import com.mercadolibre.urlshortener.exception.UrlNotFoundException;
import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.repository.UrlMappingRepository;
import com.mercadolibre.urlshortener.util.Base62Encoder;
import com.mercadolibre.urlshortener.util.UniqueIdGenerator;
import com.mercadolibre.urlshortener.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlShorteningService {

    private final UrlMappingRepository repository;
    private final UniqueIdGenerator idGenerator;

    @Autowired
    public UrlShorteningService(UrlMappingRepository repository, UniqueIdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    public UrlMapping createShortUrl(String originalUrl) {
        UrlValidator.validateUrl(originalUrl);
        long uniqueId = idGenerator.generateUniqueId();
        String encodedId = Base62Encoder.encode(uniqueId);
        UrlMapping urlMapping = new UrlMapping(encodedId, originalUrl);
        return repository.save(urlMapping);
    }

    public String getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = repository.findById(shortUrl).orElseThrow(() -> new UrlNotFoundException("URL no encontrada."));
        return urlMapping.getOriginalUrl();
    }

    public void deleteUrl(String shortUrl) {
        repository.deleteById(shortUrl);
    }
}

