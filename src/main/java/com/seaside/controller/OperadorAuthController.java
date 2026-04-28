package com.seaside.controller;

import com.seaside.model.Operador;
import com.seaside.service.OperadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/operadores/auth")
public class OperadorAuthController {

    @Autowired
    private OperadorService operadorService;

    // POST /api/operadores/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String usuario = body.get("usuario");
        String contrasena = body.get("contrasena");

        if (usuario == null || contrasena == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Usuario y contraseña son obligatorios"));
        }

        return operadorService.autenticar(usuario, contrasena)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario o contraseña incorrectos")));
    }
}