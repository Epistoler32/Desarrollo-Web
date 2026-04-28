package com.seaside.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 * Captura excepciones de dominio y las traduce a respuestas HTTP con
 * mensajes de error legibles para el frontend.
 */
@RestControllerAdvice
public class globalExceptionHandler {

    /** Captura ProductNotFoundException y devuelve 404 con mensaje descriptivo. */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}
