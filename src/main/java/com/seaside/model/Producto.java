package com.seaside.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    private String imageUrl;       // ruta relativa, ej: /resources/IMGS/Atun.jpeg
    private Integer tiempoMinutos; // tiempo de preparación en minutos
    private boolean tieneAlergenos;
}