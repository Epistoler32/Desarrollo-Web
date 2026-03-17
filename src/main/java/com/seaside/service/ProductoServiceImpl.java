package com.seaside.service;

import com.seaside.errors.ProductNotFoundException;
import com.seaside.model.Producto;
import com.seaside.repository.CategoriaRepository;
import com.seaside.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Producto searchById(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Collection<Producto> getAllProducts() {
        return productoRepository.findAll();
    }

    @Override
    public Collection<Producto> searchByCategory(String category) {
        return productoRepository.findByCategoria_Nombre(category);
    }

    @Override
    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    // Cuando el formulario envía solo el id de la categoria

    @Override
    public void saveWithCategoria(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            producto.setCategoria(
                    categoriaRepository.findById(producto.getCategoria().getId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Categoría no encontrada: " + producto.getCategoria().getId()))
            );
        }
        productoRepository.save(producto);
    }

    @Override
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }
}