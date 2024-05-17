package com.mercadolibre.urlshortener.controller;

import com.mercadolibre.urlshortener.model.UrlMapping;
import com.mercadolibre.urlshortener.model.dto.ShortUrlDto;
import com.mercadolibre.urlshortener.model.dto.UrlDto;
import com.mercadolibre.urlshortener.service.UrlShorteningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UrlControllerTest {

    @Mock
    private UrlShorteningService urlShorteningService;

    @InjectMocks
    private UrlController urlController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
    }

    @Test
    public void testCreateShortUrl() throws Exception {
        UrlDto urlDto = new UrlDto("https://example.com");
        UrlMapping urlMapping = new UrlMapping("abc123", "https://example.com");
        ShortUrlDto shortUrlDto = new ShortUrlDto("abc123");

        when(urlShorteningService.createShortUrl(any(String.class))).thenReturn(urlMapping);

        mockMvc.perform(post("/api/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(urlDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value(shortUrlDto.getShortUrl()));
    }

    @Test
    public void testGetOriginalUrl() throws Exception {
        String shortUrl = "abc123";
        String originalUrl = "https://example.com";

        when(urlShorteningService.getOriginalUrl(anyString())).thenReturn(originalUrl);

        mockMvc.perform(get("/api/url/{shortUrl}", shortUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(originalUrl));
    }

    @Test
    public void testDeleteShortUrl() throws Exception {
        String shortUrl = "abc123";

        doNothing().when(urlShorteningService).deleteUrl(anyString());

        mockMvc.perform(delete("/api/url/{shortUrl}", shortUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUrlStats() throws Exception {
        String shortUrl = "abc123";
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setId(shortUrl);
        urlMapping.setAccessCount(10);
        urlMapping.setFirstAccess(LocalDateTime.of(2024, 5, 16, 12, 0));
        urlMapping.setLastAccess(LocalDateTime.of(2024, 5, 16, 12, 10));

        when(urlShorteningService.getStats(anyString())).thenReturn(urlMapping);

        mockMvc.perform(get("/api/url/stats/{shortUrl}", shortUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessCount").value(10))
                .andExpect(jsonPath("$.firstAccess").value("2024-05-16T12:00:00"))
                .andExpect(jsonPath("$.lastAccess").value("2024-05-16T12:10:00"));
    }

}