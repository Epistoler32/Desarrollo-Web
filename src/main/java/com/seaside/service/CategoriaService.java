package com.seaside.service;

import com.seaside.model.Categoria;
import java.util.Collection;
import java.util.List;

/**
 * Contrato de servicio para la consulta de categorías del menú.
 * Las categorías agrupan los productos (Entradas, Platos Fuertes, etc.).
 */
public interface CategoriaService {
    /** Devuelve todas las categorías sin ningún orden específico. */
    Collection<Categoria> getAllCategories();

    /** Busca una categoría por su id. */
    Categoria searchById(Integer id);

    /** Devuelve las categorías ordenadas según el orden del menú. */
    List<Categoria> getAllCategoriesOrdenadas();
}