package com.seaside.service;

import com.seaside.model.Producto;
import com.seaside.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;


@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    ProductoRepository ProductoRepository;

    @Override
    public Producto searchById(Integer id) {
        return ProductoRepository.findById(id);
    }

    @Override
    public Collection<Producto> getAllProducts() {
        return ProductoRepository.findAll();
    }

    @Override
    public Collection<Producto> searchByCategory(String category) {
        return ProductoRepository.findByCategory(category);
    }

}