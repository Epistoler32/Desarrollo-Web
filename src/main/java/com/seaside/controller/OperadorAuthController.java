package com.seaside.controller;

import com.seaside.dto.LoginResponseDTO;
import com.seaside.model.Operador;
import com.seaside.security.JwtTokenGenerator;
import com.seaside.service.OperadorService;
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
Controlador REST para autenticación de operadores.

AGREGADO
- login: usa AuthenticationManager + JWT, retorna LoginResponseDTO.
- El operador se autentica con "usuario" (no correo) como username.
 */
@RestController
@RequestMapping("/api/operadores/auth")
public class OperadorAuthController {

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    /*
    Autentica un operador y retorna un JWT token.
    El campo "usuario" es el username en la tabla users.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String usuario   = body.get("usuario");
        String contrasena = body.get("contrasena");

        if (usuario == null || contrasena == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Usuario y contraseña son obligatorios"));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario, contrasena)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenGenerator.generateToken(authentication);

            Optional<Operador> op = operadorService.autenticar(usuario, contrasena);
            Integer opId = op.map(Operador::getId).orElse(null);

            return ResponseEntity.ok(new LoginResponseDTO(token, usuario, "OPERADOR", opId));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos"));
        }
    }
}