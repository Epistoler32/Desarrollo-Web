package com.seaside.service;

import com.seaside.model.Producto;
import com.seaside.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> getPlatosFuertes() {
        return productoRepository.findByCategoria("platos_fuertes");
    }

    @Override
    public List<Producto> getPostresYBebidas() {
        return productoRepository.findByCategoria("postres_y_bebidas");
    }
}
