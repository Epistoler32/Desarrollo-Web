package com.seaside.service;

import com.seaside.model.Adicionales;
import java.util.List;

/**
 * Contrato de servicio para la gestión de adicionales del menú.
 * Adicionales son ingredientes o extras que el cliente puede agregar a su
 * pedido.
 */
public interface AdicionalService {

    /** Devuelve todos los adicionales disponibles. */
    List<Adicionales> findAll();

    /** Filtra adicionales por categoría. */
    List<Adicionales> findByCategoria(Integer categoriaId);

    /** Busca un adicional por su id; devuelve null si no existe. */
    Adicionales findById(Integer id);

    /** Guarda o actualiza un adicional (resuelve la categoría automáticamente). */
    Adicionales save(Adicionales adicional);

    /** Elimina un adicional por su id. */
    void delete(Integer id);
}