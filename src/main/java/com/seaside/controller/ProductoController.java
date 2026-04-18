package com.seaside.controller;

import com.seaside.model.Adicionales;
import com.seaside.model.Producto;
import com.seaside.service.AdicionalService;
import com.seaside.service.CategoriaService;
import com.seaside.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AdicionalService adicionalService;

    @GetMapping
    public ResponseEntity<Collection<Producto>> listProducts() {
        return ResponseEntity.ok(productoService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable("id") Integer id) {
        Producto product = productoService.searchById(id);

        Set<Adicionales> asignados = product.getAdicionales();
        List<Adicionales> adicionalesResult;

        if (asignados != null && !asignados.isEmpty()) {
            // Usar los asignados explícitamente
            adicionalesResult = asignados.stream()
                    .sorted(java.util.Comparator.comparing(Adicionales::getNombre))
                    .collect(java.util.stream.Collectors.toList());
        } else {
            // Fallback: adicionales de la misma categoría del producto
            Integer categoriaId = product.getCategoria() != null
                    ? product.getCategoria().getId()
                    : null;
            if (categoriaId != null) {
                adicionalesResult = adicionalService.findByCategoria(categoriaId);
            } else {
                adicionalesResult = List.of();
            }
        }

        return ResponseEntity.ok(Map.of(
                "product", product,
                "adicionales", adicionalesResult
        ));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody Producto producto) {
        productoService.saveWithCategoria(producto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Integer id, @RequestBody Producto producto) {
        producto.setId(id);
        productoService.saveWithCategoria(producto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/adicionales")
    public ResponseEntity<Void> updateAdicionales(
            @PathVariable("id") Integer id,
            @RequestBody List<Integer> adicionalIds) {
        productoService.updateAdicionales(id, adicionalIds);
        return ResponseEntity.ok().build();
    }
}