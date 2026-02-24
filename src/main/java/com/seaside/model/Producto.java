package com.seaside.model;

/**
 * Simple data carrier for a product.  There are no persistence annotations
 * because the repository will be a lightweight in‑memory map; the class is
 * just a normal POJO.
 */
public class Producto {
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;

    public Producto() {
        // no-arg constructor for deserialization/frameworks
    }

    public Producto(String nombre, String descripcion, double precio, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
