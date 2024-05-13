package com.mercadolibre.urlshortener.util;

import com.mercadolibre.urlshortener.exception.InvalidUrlFormatException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidator {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?://)?([\\w.-]+)(\\.[a-zA-Z]{2,})([/?#].*)?$", Pattern.CASE_INSENSITIVE);

    public static void validateUrl(String url) {
        if (!StringUtils.hasText(url)) {
            throw new InvalidUrlFormatException("La URL no puede estar vacía");
        }

        if (isValidUrlWithoutScheme(url)) {
            return;
        }

        throw new InvalidUrlFormatException("Formato de URL inválido: " + url);
    }

    private static boolean isValidUrlWithoutScheme(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
}
