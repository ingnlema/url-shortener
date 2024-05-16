package com.mercadolibre.urlshortener.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("UrlMapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlMapping {
    @Id
    private String id;
    private String originalUrl;
    private long accessCount = 0;
    private LocalDateTime firstAccess;
    private LocalDateTime lastAccess;

    public UrlMapping(String shortUrl, String originalUrl) {
        this.id = shortUrl;
        this.originalUrl = originalUrl;
        this.accessCount = 0;
        this.firstAccess = null;
        this.lastAccess = null;
    }
}
