package com.seaside.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Adicionales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private double precio;

    @Column(length = 200, nullable = false, unique = true)
    private String imagenURL;

    @Column(nullable = false)
    private Integer tiempoPreparacion;

    @Column(nullable = false)
    private boolean tieneAlergenos;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Adicionales(String nombre, String descripcion, double precio,
                       String imagenURL, Integer tiempoPreparacion,
                       boolean tieneAlergenos, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenURL = imagenURL;
        this.tiempoPreparacion = tiempoPreparacion;
        this.tieneAlergenos = tieneAlergenos;
        this.categoria = categoria;
    }
}