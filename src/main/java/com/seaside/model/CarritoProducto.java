package com.seaside.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carrito_producto")
public class CarritoProducto {

    @EmbeddedId
    private CarritoProductoId id;

    @ManyToOne
    @MapsId("carritoId")
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @MapsId("productoId")
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public CarritoProducto(Carrito carrito, Producto producto) {
        this.carrito = carrito;
        this.producto = producto;
        // El id se construye solo cuando ambas entidades ya están persistidas
        if (carrito.getId() != null && producto.getId() != null) {
            this.id = new CarritoProductoId(carrito.getId(), producto.getId());
        }
    }
}