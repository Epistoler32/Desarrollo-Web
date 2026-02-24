package com.seaside.model;

public class Producto {
    private String nombre;
    private String descripcion;
    private double precio;

    public Producto() {
        // Required for Jackson
    }
    public Producto(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }
}