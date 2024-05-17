package com.mercadolibre.urlshortener.model.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.*;


@Data
@Getter
@Setter
@NoArgsConstructor
public class UrlStatsDto {
    private long accessCount;
    private String firstAccess;
    private String lastAccess;

    public UrlStatsDto(long accessCount, LocalDateTime firstAccess, LocalDateTime lastAccess) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.accessCount = accessCount;
        this.firstAccess = firstAccess.format(formatter);
        this.lastAccess = lastAccess.format(formatter);
    }

}