package com.seaside.controller;

import com.seaside.model.Pedido;
import com.seaside.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // GET /api/pedidos
    // Acepta ?activos=true para filtrar solo los pedidos activos
    @GetMapping
    public ResponseEntity<List<Pedido>> getAll(
            @RequestParam(name = "activos", required = false, defaultValue = "false") boolean activos) {
        List<Pedido> result = activos ? pedidoService.findActivos() : pedidoService.findAll();
        return ResponseEntity.ok(result);
    }

    // GET /api/pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado con id: " + id)));
    }

    // PATCH /api/pedidos/{id}/estado
    // Body: { "estado": "EN_CAMINO" }
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'estado' es obligatorio"));
        }
        return pedidoService.findById(id)
                .<ResponseEntity<?>>map(p -> {
                    pedidoService.actualizarEstado(id, nuevoEstado);
                    return ResponseEntity.ok(Map.of("message", "Estado actualizado"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado con id: " + id)));
    }

    // PATCH /api/pedidos/{id}/domiciliario
    // Body: { "domiciliarioId": 2 }
    @PatchMapping("/{id}/domiciliario")
    public ResponseEntity<?> asignarDomiciliario(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {

        Integer domiciliarioId = body.get("domiciliarioId");
        if (domiciliarioId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'domiciliarioId' es obligatorio"));
        }
        try {
            pedidoService.asignarDomiciliario(id, domiciliarioId);
            return ResponseEntity.ok(Map.of("message", "Domiciliario asignado"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/pedidos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .<ResponseEntity<?>>map(p -> {
                    pedidoService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado con id: " + id)));
    }
}