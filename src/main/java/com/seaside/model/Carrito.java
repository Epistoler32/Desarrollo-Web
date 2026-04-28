package com.seaside.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa el carrito de compras de un cliente.
 * Está relacionado 1:1 con Cliente y 1:N con CarritoProducto.
 * La persistencia del estado del carrito en el frontend se hace localmente
 * (localStorage), pero el carrito en BD sirve como respaldo.
 */
@Data
@NoArgsConstructor
@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime ultimaActualizacion;

    @JsonIgnore
    @OneToOne(mappedBy = "carrito")
    private Cliente cliente;

    // Relación con los productos del carrito
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProducto> carritoProductos = new ArrayList<>();

    public Carrito(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Integer getId() {
        return id;
    }
}