package com.seaside.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seaside.dto.DomiciliarioDTO;
import com.seaside.dto.ItemPedidoResponse;
import com.seaside.dto.PedidoRequest;
import com.seaside.model.Cliente;
import com.seaside.model.Pedido;
import com.seaside.service.ClienteService;
import com.seaside.service.DomiciliarioService;
import com.seaside.service.PedidoService;

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

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DomiciliarioService domiciliarioService;

    // ════════════════════════════════════════════════════════════════════
    //  GET /api/pedidos/mis-pedidos  (solo para el cliente autenticado)
    // ════════════════════════════════════════════════════════════════════

    /**
     * Retorna los pedidos del cliente autenticado vía JWT.
     * No requiere clienteId en el request; se extrae del token.
     */
    @GetMapping("/mis-pedidos")
    public ResponseEntity<?> getMisPedidos(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No autenticado."));
        }
        Cliente cliente = clienteService.buscarPorCorreo(auth.getName()).orElse(null);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cliente no encontrado."));
        }
        return ResponseEntity.ok(pedidoService.findByClienteId(cliente.getId()));
    }

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
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequest request, Authentication auth) {
        // Si el llamador es un cliente autenticado, inyectar su id desde el JWT
        // (nunca confiar en el clienteId del body para clientes)
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            clienteService.buscarPorCorreo(auth.getName())
                    .ifPresent(c -> request.setClienteId(c.getId()));
        }
        try {
            Pedido pedido = pedidoService.crearPedido(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  GET /api/pedidos/{id}/domiciliario
    // ════════════════════════════════════════════════════════════════════

    /**
     * Retorna el domiciliario asignado a un pedido.
     * Accesible para CLIENTE, OPERADOR y ADMINISTRADOR (cubierto por la regla /api/pedidos/**).
     */
    @GetMapping("/{id}/domiciliario")
    public ResponseEntity<?> getDomiciliario(@PathVariable Integer id) {
        return pedidoService.findById(id)
                .<ResponseEntity<?>>map(p -> {
                    if (p.getDomiciliarioId() == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Sin domiciliario asignado"));
                    }
                    return domiciliarioService.findById(p.getDomiciliarioId())
                            .<ResponseEntity<?>>map(d -> ResponseEntity.ok(DomiciliarioDTO.from(d)))
                            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(Map.of("error", "Domiciliario no encontrado")));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Pedido no encontrado con id: " + id)));
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