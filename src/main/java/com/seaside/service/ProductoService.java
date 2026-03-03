package com.seaside.service;

import com.seaside.model.Producto;

import java.util.Collection;

public interface ProductoService {
    Producto searchById(Integer id);
    Collection<Producto> getAllProducts();
    Collection<Producto> searchByCategory(String category);

    void save(Producto producto);
    void delete(Integer id);
}
