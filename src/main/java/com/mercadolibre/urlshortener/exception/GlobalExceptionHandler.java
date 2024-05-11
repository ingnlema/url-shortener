package com.mercadolibre.urlshortener.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Ha ocurrido un error inesperado. Por favor, intente de nuevo más tarde.";
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT);
    }
}
