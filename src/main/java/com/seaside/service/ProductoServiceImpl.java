package com.seaside.service;

import com.seaside.errors.ProductNotFoundException;
import com.seaside.model.Adicionales;
import com.seaside.model.Producto;
import com.seaside.repository.AdicionalesRepository;
import com.seaside.repository.CarritoProductoRepository;
import com.seaside.repository.CategoriaRepository;
import com.seaside.repository.ItemPedidoRepository;
import com.seaside.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private AdicionalesRepository adicionalesRepository;

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

    @Override
    public void saveWithCategoria(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            producto.setCategoria(
                    categoriaRepository.findById(producto.getCategoria().getId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Categoría no encontrada: " + producto.getCategoria().getId())));
        }
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        carritoProductoRepository.deleteByProductoId(id);
        itemPedidoRepository.deleteByProductoId(id);
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateAdicionales(Integer productoId, List<Integer> adicionalIds) {
        Producto producto = searchById(productoId);
        Set<Adicionales> adicionales = new HashSet<>(adicionalesRepository.findAllById(adicionalIds));
        producto.setAdicionales(adicionales);
        productoRepository.save(producto);
    }


    @Override
    public List<Adicionales> getAdicionalesParaProducto(Integer productoId) {
        Producto producto = searchById(productoId);

        Set<Adicionales> asignados = producto.getAdicionales();

        if (asignados != null && !asignados.isEmpty()) {
            return asignados.stream()
                    .sorted(Comparator.comparing(Adicionales::getNombre))
                    .collect(Collectors.toList());
        }

        // Fallback por categoría
        if (producto.getCategoria() != null) {
            return adicionalesRepository.findByCategoria_Id(producto.getCategoria().getId());
        }

        return new ArrayList<>();
    }
}