package com.mercadolibre.urlshortener.resource;

import com.mercadolibre.urlshortener.model.UrlMapping;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class UrlResource extends RepresentationModel<UrlResource> {

    private final String id;
    private final String originalUrl;

    public UrlResource(UrlMapping urlMapping) {
        this.id = urlMapping.getId();
        this.originalUrl = urlMapping.getOriginalUrl();
    }

    public String getId() {
        return id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
