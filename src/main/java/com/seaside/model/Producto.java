package com.seaside.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private double precio;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(length = 200)
    private String imageUrl;

    @Column(nullable = false)
    private Integer tiempoMinutos;

    @Column(nullable = false)
    private boolean tieneAlergenos;

    // Describe cuáles alérgenos contiene
    @Column(length = 150)
    private String descripcionAlergenos;

    // Constructor con id 
    public Producto(Integer id, String nombre, String descripcion, double precio,
                    Categoria categoria, String imageUrl,
                    Integer tiempoMinutos, boolean tieneAlergenos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.imageUrl = imageUrl;
        this.tiempoMinutos = tiempoMinutos;
        this.tieneAlergenos = tieneAlergenos;
    }

    // Constructor sin id ni descripcionAlergenos
    public Producto(String nombre, String descripcion, double precio, Categoria categoria,
                    String imageUrl, Integer tiempoMinutos, boolean tieneAlergenos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.imageUrl = imageUrl;
        this.tiempoMinutos = tiempoMinutos;
        this.tieneAlergenos = tieneAlergenos;
    }

    // Constructor completo sin id
    public Producto(String nombre, String descripcion, double precio, Categoria categoria,
                    String imageUrl, Integer tiempoMinutos, boolean tieneAlergenos,
                    String descripcionAlergenos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.imageUrl = imageUrl;
        this.tiempoMinutos = tiempoMinutos;
        this.tieneAlergenos = tieneAlergenos;
        this.descripcionAlergenos = descripcionAlergenos;
    }
}