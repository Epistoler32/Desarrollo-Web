package com.seaside.controller;

import com.seaside.dto.AdministradorDTO;
import com.seaside.dto.LoginResponseDTO;
import com.seaside.model.Administrador;
import com.seaside.security.JwtTokenGenerator;
import com.seaside.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/*
Controlador REST para autenticación de administradores.

AGRAGADO
- login: usa AuthenticationManager + JWT, retorna LoginResponseDTO con token.
*/
@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    /*
    Autentica un administrador y retorna un JWT token.
    El login del admin usa correo como username.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String correo    = body.get("correo");
        String contrasena = body.get("contrasena");

        if (correo == null || contrasena == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Correo y contraseña son obligatorios"));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, contrasena)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenGenerator.generateToken(authentication);

            // Obtener datos del admin para incluir en la respuesta (sin contraseña)
            Optional<Administrador> admin = administradorService.autenticar(correo, contrasena);
            Integer adminId = admin.map(Administrador::getId).orElse(null);

            return ResponseEntity.ok(new LoginResponseDTO(token, correo, "ADMINISTRADOR", adminId));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Correo o contraseña incorrectos"));
        }
    }
}