package com.seaside.controller;

import com.seaside.model.Pedido;
import com.seaside.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // GET /api/pedidos
    // Acepta ?activos=true para filtrar solo los pedidos activos
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll(
            @RequestParam(name = "activos", required = false, defaultValue = "false") boolean activos) {
        List<Pedido> result = activos ? pedidoService.findActivos() : pedidoService.findAll();
        return ResponseEntity.ok(result.stream().map(this::toDto).collect(Collectors.toList()));
    }

    // GET /api/pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(toDto(p)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado con id: " + id)));
    }

    // PATCH /api/pedidos/{id}/estado
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

    private Map<String, Object> toDto(Pedido p) {
        // Buscar el domiciliario asignado (relación inversa: Domiciliario.pedido)
        // El id del domiciliario se expone como campo plano para el frontend
        Integer domiciliarioId = pedidoService.getDomiciliarioIdByPedido(p.getId());

        Map<String, Object> clienteDto = Map.of(
                "id", p.getCliente().getId(),
                "nombre", p.getCliente().getNombre(),
                "apellido", p.getCliente().getApellido()
        );

        java.util.LinkedHashMap<String, Object> dto = new java.util.LinkedHashMap<>();
        dto.put("id", p.getId());
        dto.put("fechaCreacion", p.getFechaCreacion());
        dto.put("fechaEntrega", p.getFechaEntrega());
        dto.put("estado", p.getEstado());
        dto.put("total", p.getTotal());
        dto.put("cliente", clienteDto);
        dto.put("domiciliarioId", domiciliarioId); // null si no tiene asignado
        return dto;
    }
}