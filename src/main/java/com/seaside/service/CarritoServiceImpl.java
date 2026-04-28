package com.seaside.service;

import com.seaside.model.*;
import com.seaside.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de carrito.
 * Centraliza la lógica de negocio para evitar que los controladores
 * accedan directamente a los repositorios (arquitectura en capas).
 */
@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public boolean existeCarrito(Integer carritoId) {
        return carritoRepository.existsById(carritoId);
    }

    @Override
    public List<CarritoProducto> getItems(Integer carritoId) {
        return carritoProductoRepository.findByCarritoId(carritoId);
    }

    /**
     * Agrega un producto al carrito incrementando la cantidad si ya existe,
     * o creando un nuevo ítem si no existe.
     */
    @Override
    @Transactional
    public CarritoProducto addItem(Integer carritoId, Integer productoId, Integer cantidad) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado: " + carritoId));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoId));

        CarritoProductoId cpId = new CarritoProductoId(carritoId, productoId);
        CarritoProducto cp = carritoProductoRepository.findById(cpId)
                .orElse(new CarritoProducto(carrito, producto, 0));

        cp.setCantidad(cp.getCantidad() + cantidad);
        if (cp.getId() == null)
            cp.setId(cpId);

        carritoProductoRepository.save(cp);
        carrito.setUltimaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);

        return cp;
    }

    /** Actualiza la cantidad exacta de un ítem ya existente en el carrito. */
    @Override
    @Transactional
    public Optional<CarritoProducto> updateCantidad(Integer carritoId, Integer productoId, Integer cantidad) {
        CarritoProductoId cpId = new CarritoProductoId(carritoId, productoId);
        return carritoProductoRepository.findById(cpId).map(cp -> {
            cp.setCantidad(cantidad);
            carritoProductoRepository.save(cp);
            carritoRepository.findById(carritoId).ifPresent(c -> {
                c.setUltimaActualizacion(LocalDateTime.now());
                carritoRepository.save(c);
            });
            return cp;
        });
    }

    /** Elimina un ítem específico del carrito. Retorna false si no existía. */
    @Override
    @Transactional
    public boolean removeItem(Integer carritoId, Integer productoId) {
        CarritoProductoId cpId = new CarritoProductoId(carritoId, productoId);
        if (!carritoProductoRepository.existsById(cpId))
            return false;

        carritoProductoRepository.deleteById(cpId);
        carritoRepository.findById(carritoId).ifPresent(c -> {
            c.setUltimaActualizacion(LocalDateTime.now());
            carritoRepository.save(c);
        });
        return true;
    }

    /**
     * Vacía todos los ítems del carrito. Retorna false si el carrito no existía.
     */
    @Override
    @Transactional
    public boolean clearCarrito(Integer carritoId) {
        if (!carritoRepository.existsById(carritoId))
            return false;

        carritoProductoRepository.deleteByCarritoId(carritoId);
        carritoRepository.findById(carritoId).ifPresent(c -> {
            c.setUltimaActualizacion(LocalDateTime.now());
            carritoRepository.save(c);
        });
        return true;
    }
}
