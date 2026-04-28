package com.seaside.controller;

import com.seaside.dto.ItemPedidoResponse;
import com.seaside.dto.PedidoRequest;
import com.seaside.model.ItemPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de pedidos.
 * Toda la lógica de negocio se delega a {@link PedidoService}.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // GET /api/pedidos | GET /api/pedidos?activos=true
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

    // PATCH /api/pedidos/{id}/estado — body: { "estado": "EN_CAMINO" }
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

    // PATCH /api/pedidos/{id}/domiciliario — body: { "domiciliarioId": 2 }
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

    // POST /api/pedidos
    // valida usuario/contraseña contra la tabla de operadores existente
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody com.seaside.dto.PedidoRequest request) {
        try {
            Pedido pedido = pedidoService.crearPedido(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // GET /api/pedidos/{id}/items
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getItems(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .<ResponseEntity<?>>map(p -> {
                    List<ItemPedido> items = pedidoService.getItemsByPedidoId(id);
                    return ResponseEntity.ok(items);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado con id: " + id)));
    }
}