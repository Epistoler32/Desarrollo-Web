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
    private String imageUrl;       // ruta relativa, ej: /resources/IMGS/Atun.jpeg
    @Column(nullable = false)
    private Integer tiempoMinutos; // tiempo de preparación en minutos
    @Column(nullable = false)
    private boolean tieneAlergenos;

    public Producto(String nombre, String descripcion, double precio, Categoria categoria, String imageUrl, Integer tiempoMinutos, boolean tieneAlergenos) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.precio = precio;
    this.categoria = categoria;
    this.imageUrl = imageUrl;
    this.tiempoMinutos = tiempoMinutos;
    this.tieneAlergenos = tieneAlergenos;
}
}