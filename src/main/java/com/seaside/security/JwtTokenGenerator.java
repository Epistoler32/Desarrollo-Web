package com.seaside.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// Componente para la gestión de JSON Web Tokens jwt

@Component
public class JwtTokenGenerator {

    // Tiempo de expiración del token - 24 horas en millisegundos
    private static final long JWT_EXPIRATION = 86400000L;

    // Clave secreta generada aleatoriamente con HS512

    private final Key jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Genera un JWT token a partir de la autenticación exitosa
    
    public String generateToken(Authentication authentication) {
        String username   = authentication.getName();
        Date   issuedAt   = new Date();
        Date   expiration = new Date(issuedAt.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Extrae el username (subject) del JWT token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Valida que el JWT token sea válido: firma correcta y no expirado
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extrae todos los claims del token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}