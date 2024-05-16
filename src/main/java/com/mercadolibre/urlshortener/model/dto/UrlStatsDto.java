package com.mercadolibre.urlshortener.model.dto;

import java.time.LocalDateTime;

import lombok.*;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlStatsDto {
    private long accessCount;
    private LocalDateTime firstAccess;
    private LocalDateTime lastAccess;

}