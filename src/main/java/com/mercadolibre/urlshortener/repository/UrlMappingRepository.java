package com.mercadolibre.urlshortener.repository;

import com.mercadolibre.urlshortener.model.UrlMapping;
import org.springframework.data.repository.CrudRepository;

public interface UrlMappingRepository extends CrudRepository<UrlMapping, String> {
}
