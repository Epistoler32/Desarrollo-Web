package com.seaside.controller;

import com.seaside.dto.ItemPedidoResponse;
import com.seaside.dto.PedidoRequest;
import com.seaside.model.ItemPedido;
import com.seaside.model.Pedido;
import com.seaside.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de pedidos.
 *
 * Todos los métodos retornan ResponseEntity con el código HTTP correcto:
 *  - 200 OK:         consulta exitosa
 *  - 201 CREATED:    recurso creado
 *  - 204 NO CONTENT: eliminación exitosa
 *  - 400 BAD REQUEST: datos inválidos o error de negocio
 *  - 404 NOT FOUND:  recurso no encontrado
 *
 * Esto permite que las pruebas de controlador (con MockMvc) puedan
 * verificar no solo el cuerpo de la respuesta sino también el código HTTP.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // ════════════════════════════════════════════════════════════════════
    //  GET /api/pedidos
    //  GET /api/pedidos?activos=true
    //  GET /api/pedidos?clienteId=X
    // ════════════════════════════════════════════════════════════════════

    /**
     * Retorna todos los pedidos, filtrados por activos o por clienteId.
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> getAll(
            @RequestParam(name = "activos",   required = false, defaultValue = "false") boolean activos,
            @RequestParam(name = "clienteId", required = false) Integer clienteId) {

        List<Pedido> result;
        if (clienteId != null) {
            result = pedidoService.findByClienteId(clienteId);
        } else {
            result = activos ? pedidoService.findActivos() : pedidoService.findAll();
        }
        return ResponseEntity.ok(result);
    }

    // ════════════════════════════════════════════════════════════════════
    //  GET /api/pedidos/{id}
    // ════════════════════════════════════════════════════════════════════

    /**
     * Busca un pedido por su ID.
     * Retorna 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado con id: " + id)));
    }

    // ════════════════════════════════════════════════════════════════════
    //  PATCH /api/pedidos/{id}/estado
    // ════════════════════════════════════════════════════════════════════

    /**
     * Actualiza el estado de un pedido existente.
     * Retorna 400 si el campo 'estado' no viene en el body.
     * Retorna 404 si el pedido no existe.
     */
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

    // ════════════════════════════════════════════════════════════════════
    //  PATCH /api/pedidos/{id}/domiciliario
    // ════════════════════════════════════════════════════════════════════

    /**
     * Asigna un domiciliario disponible a un pedido.
     */
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

    // ════════════════════════════════════════════════════════════════════
    //  DELETE /api/pedidos/{id}
    // ════════════════════════════════════════════════════════════════════

    /**
     * Elimina un pedido por su ID.
     * Retorna 204 NO CONTENT si se eliminó correctamente.
     * Retorna 404 si el pedido no existe.
     */
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

    // ════════════════════════════════════════════════════════════════════
    //  POST /api/pedidos
    // ════════════════════════════════════════════════════════════════════

    /**
     * Crea un nuevo pedido a partir del DTO recibido desde el frontend.
     * Retorna 201 CREATED con el pedido creado.
     * Retorna 400 BAD REQUEST si los datos son inválidos.
     */
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequest request) {
        try {
            Pedido pedido = pedidoService.crearPedido(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  GET /api/pedidos/{id}/items
    // ════════════════════════════════════════════════════════════════════

    /**
     * Retorna los ítems (con adicionales) de un pedido específico.
     */
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getItems(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .<ResponseEntity<?>>map(p -> {
                    List<ItemPedidoResponse> items = pedidoService.getItemsByPedidoId(id)
                            .stream()
                            .map(ItemPedidoResponse::from)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(items);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado con id: " + id)));
    }
}