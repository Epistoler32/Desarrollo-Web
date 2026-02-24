package com.seaside.repository;

import com.seaside.model.Producto;

import java.util.List;


public interface ProductoRepository {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findAll();
    List<Producto> findByID(String nombre);
}
