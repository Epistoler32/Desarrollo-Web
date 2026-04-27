package com.seaside.controller;

import com.seaside.model.*;
import com.seaside.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired private CarritoRepository carritoRepository;
    @Autowired private CarritoProductoRepository carritoProductoRepository;
    @Autowired private ProductoRepository productoRepository;

    //GET items del carrito
    public ResponseEntity<?> getItems(@PathVariable Integer carritoId) {
        if (!carritoRepository.existsById(carritoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrito no encontrado: " + carritoId));
        }
        List<CarritoProducto> items = carritoProductoRepository.findByCarritoId(carritoId);
        return ResponseEntity.ok(items);
    }

    // POST agregar / actualizar producto
    @PostMapping("/{carritoId}/items")
    @Transactional
    public ResponseEntity<?> addItem(
            @PathVariable Integer carritoId,
            @RequestBody Map<String, Integer> body) {

        Integer productoId = body.get("productoId");
        Integer cantidad   = body.getOrDefault("cantidad", 1);

        if (productoId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "productoId es obligatorio"));
        }

        Carrito carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrito no encontrado: " + carritoId));
        }

        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Producto no encontrado: " + productoId));
        }

        CarritoProductoId cpId = new CarritoProductoId(carritoId, productoId);
        CarritoProducto cp = carritoProductoRepository.findById(cpId)
                .orElse(new CarritoProducto(carrito, producto, 0));

        cp.setCantidad(cp.getCantidad() + cantidad);
        if (cp.getId() == null) cp.setId(cpId);

        carritoProductoRepository.save(cp);
        carrito.setUltimaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);

        return ResponseEntity.ok(cp);
    }

    //PATCH cambiar cantidad exacta
    @PatchMapping("/{carritoId}/items/{productoId}")
    @Transactional
    public ResponseEntity<?> updateCantidad(
            @PathVariable Integer carritoId,
            @PathVariable Integer productoId,
            @RequestBody Map<String, Integer> body) {

        Integer cantidad = body.get("cantidad");
        if (cantidad == null || cantidad < 1) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "cantidad debe ser >= 1"));
        }

        CarritoProductoId cpId = new CarritoProductoId(carritoId, productoId);
        return carritoProductoRepository.findById(cpId)
                .<ResponseEntity<?>>map(cp -> {
                    cp.setCantidad(cantidad);
                    carritoProductoRepository.save(cp);
                    carritoRepository.findById(carritoId).ifPresent(c -> {
                        c.setUltimaActualizacion(LocalDateTime.now());
                        carritoRepository.save(c);
                    });
                    return ResponseEntity.ok(cp);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Ítem no encontrado en el carrito")));
    }

    // DELETE quitar un producto 
    @DeleteMapping("/{carritoId}/items/{productoId}")
    @Transactional
    public ResponseEntity<?> removeItem(
            @PathVariable Integer carritoId,
            @PathVariable Integer productoId) {

        CarritoProductoId cpId = new CarritoProductoId(carritoId, productoId);
        if (!carritoProductoRepository.existsById(cpId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Ítem no encontrado en el carrito"));
        }
        carritoProductoRepository.deleteById(cpId);
        carritoRepository.findById(carritoId).ifPresent(c -> {
            c.setUltimaActualizacion(LocalDateTime.now());
            carritoRepository.save(c);
        });
        return ResponseEntity.noContent().build();
    }

    //DELETE vaciar carrito 
    @DeleteMapping("/{carritoId}")
    @Transactional
    public ResponseEntity<?> clearCarrito(@PathVariable Integer carritoId) {
        if (!carritoRepository.existsById(carritoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrito no encontrado: " + carritoId));
        }
        carritoProductoRepository.deleteByCarritoId(carritoId);
        carritoRepository.findById(carritoId).ifPresent(c -> {
            c.setUltimaActualizacion(LocalDateTime.now());
            carritoRepository.save(c);
        });
        return ResponseEntity.noContent().build();
    }
}