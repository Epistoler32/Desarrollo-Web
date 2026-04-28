package com.seaside.controller;

import com.seaside.model.Cliente;
import com.seaside.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para autenticación de clientes.
 * Expone los endpoints de registro y login bajo /api/auth.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Registra un nuevo cliente.
     * Devuelve 409 si el correo ya está en uso.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> procesarSignup(@RequestBody Cliente cliente) {
        if (clienteService.existeCorreo(cliente.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe una cuenta con ese correo."));
        }
        Cliente guardado = clienteService.registrarNuevo(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    /**
     * Autentica un cliente por correo y contraseña.
     * Devuelve 401 si las credenciales no coinciden.
     */
    @PostMapping("/login")
    public ResponseEntity<?> procesarLogin(@RequestBody Map<String, String> credentials) {
        String correo = credentials.get("correo");
        String contrasena = credentials.get("contrasena");

        Optional<Cliente> encontrado = clienteService.autenticar(correo, contrasena);

        if (encontrado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Correo o contraseña incorrectos."));
        }

        return ResponseEntity.ok(encontrado.get());
    }
}