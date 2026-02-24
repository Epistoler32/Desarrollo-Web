package com.seaside.service;

import com.seaside.model.Producto;
import com.seaside.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> getPlatosFuertes() {
        return productoRepository.findByCategoria("platos_fuertes");
    }

    public List<Producto> getPostresYBebidas() {
        return productoRepository.findByCategoria("postres_y_bebidas");
    }
}
