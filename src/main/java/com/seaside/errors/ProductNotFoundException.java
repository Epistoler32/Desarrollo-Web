package com.seaside.errors;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Integer id) {
        super("Producto no encontrado con id: " + id);
    }

}
