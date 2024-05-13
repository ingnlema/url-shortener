package com.mercadolibre.urlshortener.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Documentación de la API",
        version = "1.0.0",
        description = "Esta es la documentación de la API para el acortador de URLs."
))
public class OpenApiConfig {
}
