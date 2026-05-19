package com.seaside.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filtro JWT que se ejecuta una vez por cada petición HTTP


/*
Flujo:
1. Intercepta todas las peticiones entrantes.
2. Extrae el JWT del header "Authorization: Bearer <token>".
3. Valida que el token sea válido (firma + no expirado).
4. Si es válido, extrae el username y carga el UserDetails.
5. Crea un objeto de autenticación y lo guarda en el SecurityContext.
6. A partir de ese momento, Spring Security sabe quién es el usuario.

Este filtro se agrega a la SecurityFilterChain ANTES del filtro de autenticación por usuario/contraseña (UsernamePasswordAuthenticationFilter).
*/

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extraer el JWT del header Authorization
        String token = getJwtFromRequest(request);

        // 2. Validar el token y procesar la autenticación
        if (token != null && jwtTokenGenerator.validateToken(token)) {

            // 3. Extraer username del token
            String username = jwtTokenGenerator.extractUsername(token);

            // 4. Cargar los detalles del usuario desde la BD
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // 5. Crear objeto de autenticación con roles/authorities
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // null pq ya esta autenticado
                            userDetails.getAuthorities() // roles del usuario
                    );

            // 6. Agregar detalles del request (IP, session) al token de autenticación
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 7. Guardar la autenticación en el SecurityContext
            //    Desde aquí, Spring Security sabe quién es el usuario en esta petición
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 8. Pasar la petición al siguiente filtro de la cadena
        filterChain.doFilter(request, response);
    }

    // Extrae el JWT del header "Authorization".

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        }
        return null;
    }
}