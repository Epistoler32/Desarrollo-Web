package com.seaside.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de unión entre Carrito y Producto.
 * Usa clave compuesta (CarritoProductoId) para representar qué
 * productos y en qué cantidad están en el carrito de un cliente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carrito_producto")
public class CarritoProducto {

    @EmbeddedId
    private CarritoProductoId id;

    @JsonIgnore
    @ManyToOne
    @MapsId("carritoId")
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @MapsId("productoId")
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad = 1;

    public CarritoProducto(Carrito carrito, Producto producto, Integer cantidad) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
        // El id se construye solo cuando ambas entidades ya están persistidas
        if (carrito.getId() != null && producto.getId() != null) {
            this.id = new CarritoProductoId(carrito.getId(), producto.getId());
        }
    }
}