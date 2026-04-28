package com.seaside.service;

import com.seaside.model.Adicionales;
import com.seaside.model.Producto;
import java.util.Collection;
import java.util.List;

/**
 * Contrato de servicio para la gestión de productos del menú.
 * Incluye operaciones de búsqueda, persistencia y asignación de adicionales.
 */
public interface ProductoService {
    /** Busca un producto por su id; lanza ProductNotFoundException si no existe. */
    Producto searchById(Integer id);

    /** Devuelve todos los productos del menú. */
    Collection<Producto> getAllProducts();

    /** Filtra productos por categoría (nombre de la categoría). */
    Collection<Producto> searchByCategory(String category);

    /** Persiste un producto (sin resolver la categoría automáticamente). */
    void save(Producto producto);

    // Resuelve la categoría a partir del id que llega del formulario y persiste
    void saveWithCategoria(Producto producto);

    /** Elimina un producto por su id. */
    void delete(Integer id);

    /** Actualiza la lista de adicionales asociados a un producto. */
    void updateAdicionales(Integer productoId, List<Integer> adicionalIds);

    /** Devuelve los adicionales disponibles para un producto específico. */
    List<Adicionales> getAdicionalesParaProducto(Integer productoId);
}