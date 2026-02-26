package com.seaside.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

public class Producto {
    private Integer ID;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    //Image URL field hace falta
}
