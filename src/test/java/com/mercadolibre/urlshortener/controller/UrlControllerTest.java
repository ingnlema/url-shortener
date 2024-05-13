package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.resource.UrlResource;
import com.mercadolibre.urlshortener.resource.UrlResourceAssembler;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShorteningService service;

    @MockBean
    private UrlResourceAssembler assembler;

    @Test
    void testGetOriginalUrl() throws Exception {
        String id = "abc123";
        String originalUrl = "http://example.com";
        UrlMapping urlMapping = new UrlMapping(id, originalUrl);
        UrlResource urlResource = new UrlResource(urlMapping);
        urlResource.add(
                linkTo(methodOn(UrlController.class).getOriginalUrl(id)).withSelfRel(),
                linkTo(methodOn(UrlController.class).deleteShortUrl(id)).withRel("delete")
        );

        Mockito.when(service.getOriginalUrl(id)).thenReturn(originalUrl);
        Mockito.when(assembler.toModel(Mockito.any(UrlMapping.class))).thenReturn(urlResource);

        mockMvc.perform(get("/api/url/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    void testCreateShortUrl() throws Exception {
        String id = "abc123";
        String originalUrl = "http://example.com";
        UrlMapping urlMapping = new UrlMapping(id, originalUrl);
        UrlResource urlResource = new UrlResource(urlMapping);
        urlResource.add(
                linkTo(methodOn(UrlController.class).getOriginalUrl(id)).withSelfRel(),
                linkTo(methodOn(UrlController.class).deleteShortUrl(id)).withRel("delete")
        );

        Mockito.when(service.createShortUrl(Mockito.anyString())).thenReturn(urlMapping);
        Mockito.when(assembler.toModel(Mockito.any(UrlMapping.class))).thenReturn(urlResource);

        mockMvc.perform(post("/api/url/shorten").contentType("application/json").content("{\"url\":\"http://example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }
}
