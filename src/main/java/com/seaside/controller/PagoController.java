package com.seaside.controller;

import com.mercadopago.exceptions.MPApiException;
import com.seaside.dto.PagoResponseDTO;
import com.seaside.model.Pedido;
import com.seaside.service.PagoService;
import com.seaside.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PedidoService pedidoService;

    /*
     Recibe el id del pedido ya creado y genera
     la URL de pago en MercadoPago.
     Requiere rol CLIENTE.
     */
    @PostMapping("/crear-preferencia/{pedidoId}")
    public ResponseEntity<?> crearPreferencia(@PathVariable Integer pedidoId) {
        Optional<Pedido> pedidoOpt = pedidoService.findById(pedidoId);

        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            PagoResponseDTO response = pagoService.crearPreferencia(pedidoOpt.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error MercadoPago: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al crear preferencia: " + e.getMessage()));
        }
    }

    /*
     Webhook que MercadoPago llama cuando el pago cambia de estado.
     Actualiza el estado del pedido según el resultado.
     */
    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestParam(required = false) String type,
                                      @RequestBody(required = false) Map<String, Object> body) {
        System.out.println("Webhook MercadoPago recibido: " + type);
        if (body != null) System.out.println("Body: " + body);
        return ResponseEntity.ok().build();
    }
}
