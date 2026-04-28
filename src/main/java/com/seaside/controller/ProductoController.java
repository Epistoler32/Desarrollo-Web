package com.seaside.controller;

import com.seaside.model.Producto;
import com.seaside.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de productos del menú.
 * Expone CRUD completo bajo /api/products y un endpoint dedicado
 * para actualizar los adicionales de un producto.
 */
@RestController
@RequestMapping("/api/products")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /** Devuelve todos los productos del menú. */
    @GetMapping
    public ResponseEntity<Collection<Producto>> listProducts() {
        return ResponseEntity.ok(productoService.getAllProducts());
    }

    /** Devuelve un producto por id junto con sus adicionales disponibles. */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable("id") Integer id) {
        Producto product = productoService.searchById(id);
        return ResponseEntity.ok(Map.of(
                "product", product,
                "adicionales", productoService.getAdicionalesParaProducto(id)));
    }

    /** Crea un nuevo producto resolviendo la categoría automáticamente. */
    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody Producto producto) {
        productoService.saveWithCategoria(producto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /** Actualiza los datos de un producto existente. */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Integer id, @RequestBody Producto producto) {
        producto.setId(id);
        productoService.saveWithCategoria(producto);
        return ResponseEntity.ok().build();
    }

    /** Elimina un producto por su id. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** Reemplaza la lista de adicionales asociados a un producto. */
    @PutMapping("/{id}/adicionales")
    public ResponseEntity<Void> updateAdicionales(
            @PathVariable("id") Integer id,
            @RequestBody List<Integer> adicionalIds) {
        productoService.updateAdicionales(id, adicionalIds);
        return ResponseEntity.ok().build();
    }
}