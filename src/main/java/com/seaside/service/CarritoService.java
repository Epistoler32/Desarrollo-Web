package com.seaside.service;

import com.seaside.model.CarritoProducto;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión del carrito de compras.
 * Encapsula toda la lógica de negocio relacionada con el carrito,
 * evitando que los controladores accedan directamente a los repositorios.
 */
public interface CarritoService {

    /** Verifica si existe un carrito con el id dado. */
    boolean existeCarrito(Integer carritoId);

    /** Retorna todos los ítems de un carrito. */
    List<CarritoProducto> getItems(Integer carritoId);

    /**
     * Agrega un producto al carrito (o incrementa su cantidad si ya existe).
     *
     * @throws IllegalArgumentException si el carrito o el producto no existen
     */
    CarritoProducto addItem(Integer carritoId, Integer productoId, Integer cantidad);

    /**
     * Actualiza la cantidad exacta de un producto en el carrito.
     * Retorna empty si el ítem no existe.
     */
    Optional<CarritoProducto> updateCantidad(Integer carritoId, Integer productoId, Integer cantidad);

    /**
     * Elimina un producto del carrito.
     *
     * @return false si el ítem no existía
     */
    boolean removeItem(Integer carritoId, Integer productoId);

    /**
     * Vacía por completo el carrito.
     *
     * @return false si el carrito no existía
     */
    boolean clearCarrito(Integer carritoId);
}
