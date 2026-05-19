package com.seaside.controller;

import com.seaside.model.CarritoProducto;
import com.seaside.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el carrito de compras.
 * Delega toda la lógica de negocio a {@link CarritoService}.
 */
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    /** GET /api/carrito/{carritoId}/items - retorna los ítems del carrito */
    @GetMapping("/{carritoId}/items")
    public ResponseEntity<?> getItems(@PathVariable Integer carritoId) {
        if (!carritoService.existeCarrito(carritoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrito no encontrado: " + carritoId));
        }
        List<CarritoProducto> items = carritoService.getItems(carritoId);
        return ResponseEntity.ok(items);
    }

    /** POST /api/carrito/{carritoId}/items - agrega o incrementa un producto */
    @PostMapping("/{carritoId}/items")
    public ResponseEntity<?> addItem(
            @PathVariable Integer carritoId,
            @RequestBody Map<String, Integer> body) {

        Integer productoId = body.get("productoId");
        Integer cantidad = body.getOrDefault("cantidad", 1);

        if (productoId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "productoId es obligatorio"));
        }

        try {
            CarritoProducto cp = carritoService.addItem(carritoId, productoId, cantidad);
            return ResponseEntity.ok(cp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * PATCH /api/carrito/{carritoId}/items/{productoId} - actualiza cantidad exacta
     */
    @PatchMapping("/{carritoId}/items/{productoId}")
    public ResponseEntity<?> updateCantidad(
            @PathVariable Integer carritoId,
            @PathVariable Integer productoId,
            @RequestBody Map<String, Integer> body) {

        Integer cantidad = body.get("cantidad");
        if (cantidad == null || cantidad < 1) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "cantidad debe ser >= 1"));
        }

        return carritoService.updateCantidad(carritoId, productoId, cantidad)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Ítem no encontrado en el carrito")));
    }

    /**
     * DELETE /api/carrito/{carritoId}/items/{productoId} - elimina un producto del
     * carrito
     */
    @DeleteMapping("/{carritoId}/items/{productoId}")
    public ResponseEntity<?> removeItem(
            @PathVariable Integer carritoId,
            @PathVariable Integer productoId) {

        if (!carritoService.removeItem(carritoId, productoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Ítem no encontrado en el carrito"));
        }
        return ResponseEntity.noContent().build();
    }

    /** DELETE /api/carrito/{carritoId} - vacía el carrito */
    @DeleteMapping("/{carritoId}")
    public ResponseEntity<?> clearCarrito(@PathVariable Integer carritoId) {
        if (!carritoService.clearCarrito(carritoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Carrito no encontrado: " + carritoId));
        }
        return ResponseEntity.noContent().build();
    }
}