package com.seaside.controller;

import com.seaside.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/sms")
    public ResponseEntity<?> enviarSms(@RequestBody Map<String, String> body) {
        String telefono = body.get("telefono");
        String mensaje  = body.get("mensaje");

        if (telefono == null || mensaje == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "telefono y mensaje son obligatorios"));
        }

        try {
            smsService.enviarSms(telefono, mensaje);
            return ResponseEntity.ok(Map.of("message", "SMS enviado"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", e.getMessage()));
        }
    }
}