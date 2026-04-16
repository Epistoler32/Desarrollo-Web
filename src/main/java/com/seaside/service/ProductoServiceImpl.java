package com.seaside.service;

import java.util.List;
import java.util.Set;
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
import java.util.HashSet;
import java.util.Collection;

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

    /**
     * Cuando el formulario envía solo el id de la categoría (categoria.id),
     * este método resuelve el objeto Categoria completo antes de persistir.
     */
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

    /**
     * Elimina un producto limpiando primero todas las referencias que lo bloquean:
     * 1. CarritoProducto — filas de la tabla intermedia carrito_producto
     * 2. ItemPedido — items de pedidos que referencian este producto
     *
     * Sin este orden, la FK en carrito_producto e item_pedido lanza
     * ConstraintViolationException e impide el borrado.
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        // 1. Quitar el producto de todos los carritos donde esté
        carritoProductoRepository.deleteByProductoId(id);

        // 2. Quitar los items de pedido que referencian este producto
        itemPedidoRepository.deleteByProductoId(id);

        // 3. Ahora sí se puede borrar el producto sin violar FK
        productoRepository.deleteById(id);
    }

    @Transactional
    public void updateAdicionales(Integer productoId, List<Integer> adicionalIds) {
        Producto producto = searchById(productoId);
        Set<Adicionales> adicionales = new HashSet<>(adicionalesRepository.findAllById(adicionalIds));
        producto.setAdicionales(adicionales);
        productoRepository.save(producto);
    }
}