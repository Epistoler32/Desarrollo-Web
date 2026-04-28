package com.seaside.errors;

/**
 * Excepción lanzada cuando no se encuentra un producto por su id.
 * Es capturada por globalExceptionHandler y devuelta como 404.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Integer id) {
        super("Producto no encontrado con id: " + id);
    }

}
