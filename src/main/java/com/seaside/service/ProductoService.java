package com.seaside.service;

import com.seaside.model.Producto;

import java.util.Collection;
import java.util.List;

public interface ProductoService {
    public Producto searchById(Integer id);
    public Collection<Producto> getAllProducts();
    
}
