package com.mercadolibre.urlshortener.util;

import com.mercadolibre.urlshortener.exception.InvalidUrlFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UrlValidatorTest {

    @Test
    public void testValidateUrl_SuccessWithScheme() {
        // URLs válidas con esquema
        assertDoesNotThrow(() -> UrlValidator.validateUrl("http://example.com"));
        assertDoesNotThrow(() -> UrlValidator.validateUrl("https://example.com"));
        assertDoesNotThrow(() -> UrlValidator.validateUrl("http://example.com/path?query=1"));
    }

    @Test
    public void testValidateUrl_SuccessWithoutScheme() {
        // URL válida sin esquema
        assertDoesNotThrow(() -> UrlValidator.validateUrl("example.com"));
        assertDoesNotThrow(() -> UrlValidator.validateUrl("example.com/path?query=1"));
    }

    @Test
    public void testValidateUrl_EmptyUrl() {
        // URL vacía
        assertThrows(InvalidUrlFormatException.class, () -> UrlValidator.validateUrl(""));
    }

    @Test
    public void testValidateUrl_InvalidUrl() {
        // URLs con formato inválido
        assertThrows(InvalidUrlFormatException.class, () -> UrlValidator.validateUrl("http:/example.com"));
        assertThrows(InvalidUrlFormatException.class, () -> UrlValidator.validateUrl("example"));
        assertThrows(InvalidUrlFormatException.class, () -> UrlValidator.validateUrl("ftp://example.com"));
    }

    @Test
    public void testValidateUrl_NullUrl() {
        // URL nula
        assertThrows(InvalidUrlFormatException.class, () -> UrlValidator.validateUrl(null));
    }

    @Test
    public void testValidateUrl_WithSpecialCharacters() {
        // URL con caracteres especiales válidos
        assertDoesNotThrow(() -> UrlValidator.validateUrl("http://example.com/~user"));
        assertDoesNotThrow(() -> UrlValidator.validateUrl("https://example.com/path#fragment"));
    }

    @Test
    public void testValidateUrl_WithSubdomains() {
        // URL con subdominios válidos
        assertDoesNotThrow(() -> UrlValidator.validateUrl("http://sub.example.com"));
    }
}