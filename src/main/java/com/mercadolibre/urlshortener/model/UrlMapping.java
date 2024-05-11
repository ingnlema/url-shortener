package com.mercadolibre.urlshortener.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("UrlMapping")
public class UrlMapping {
    @Id
    private String id;
    private String originalUrl;

    public UrlMapping() {
    }

    public UrlMapping(String id, String originalUrl) {
        this.id = id;
        this.originalUrl = originalUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
