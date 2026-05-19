package com.seaside.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

//Manejador de errores de autenticación (401 Unauthorized)
// Se activa cuando alguien intenta acceder a un endpoint protegido sin estar autenticado o con un JWT inválido/expirado



@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, String> body = Map.of(
                "error",   "No autorizado",
                "message", "Debes iniciar sesión para acceder a este recurso",
                "path",    request.getServletPath()
        );

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}