package com.seaside.controller;

import com.seaside.model.Administrador;
import com.seaside.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    @Autowired
    private AdministradorRepository administradorRepository;

    // POST /api/admin/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String correo    = body.get("correo");
        String contrasena = body.get("contrasena");

        if (correo == null || contrasena == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Correo y contraseña son obligatorios"));
        }

        return administradorRepository.findByCorreo(correo)
                .filter(a -> a.getContrasena().equals(contrasena))
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Correo o contraseña incorrectos")));
    }
}