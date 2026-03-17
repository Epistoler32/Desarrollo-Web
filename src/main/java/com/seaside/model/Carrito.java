package com.seaside.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime ultimaActualizacion;

    // un carrito pertenece a un cliente
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