package com.seaside.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Clave primaria compuesta para la entidad CarritoProducto.
 * Combina el id del carrito y el id del producto para identificar
 * de forma única cada línea del carrito.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CarritoProductoId implements Serializable {
    private Integer carritoId;
    private Integer productoId;
}