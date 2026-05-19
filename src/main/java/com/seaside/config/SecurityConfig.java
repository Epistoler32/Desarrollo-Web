package com.seaside.config;

import com.seaside.security.AuthEntryPoint;
import com.seaside.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Configuración principal de Spring Security

// Implementa el patrón Filter Chain, decide si una petición HTTP puede llegar a los controladores o debe ser bloqueada

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Define la cadena de filtros de seguridad.
    
    /*
    Orden de configuración:
    1. Deshabilitar CSRF (no necesario en arquitectura cliente-servidor REST)
    2. Configurar manejo de errores 401
    3. Definir qué endpoints son públicos y cuáles requieren autenticación
    4. Configurar cabeceras para h2-console
    5. Definir política de sesión STATELESS (sin sesiones en servidor, usamos JWT)
    6. Agregar nuestro filtro JWT ANTES del filtro de usuario/contraseña
    */
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilitar CSRF
            .csrf(csrf -> csrf.disable())

            // 2. Manejo de error 401: devuelve JSON limpio en vez del HTML de Spring
            .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))

            // 3. Reglas de autorización por endpoint
            .authorizeHttpRequests(auth -> auth
                // Consola H2 - acceso público en desarrollo
                .requestMatchers("/h2/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // ENDPOINTS PÚBLICOS
                // Auth: login y registro de todos los tipos de usuario
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/auth/**").permitAll()
                .requestMatchers("/api/operadores/auth/**").permitAll()

                // Páginas informativas del restaurante
                .requestMatchers(HttpMethod.GET, "/api/menu/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/contacto/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/visitanos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/status").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                // ENDPOINTS PROTEGIDOS POR ROL
                // Solo ADMINISTRADOR puede gestionar productos, categorías, domiciliarios y operadores
                .requestMatchers("/api/products/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/api/adicionales/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/api/domiciliarios/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/api/operadores/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/api/clients/**").hasAuthority("ADMINISTRADOR")

                // Pedidos: accesibles para CLIENTE, OPERADOR y ADMINISTRADOR
                .requestMatchers("/api/pedidos/**")
                    .hasAnyAuthority("CLIENTE", "OPERADOR", "ADMINISTRADOR")

                // Carrito: solo el propio CLIENTE
                .requestMatchers("/api/carrito/**").hasAuthority("CLIENTE")

                // Cualquier otra petición requiere estar autenticado
                .anyRequest().authenticated()
            )

            // 4. Permitir frames para la consola H2
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // 5. Sin sesiones en el servidor: cada petición se autentica con JWT
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 6. Nuestro filtro JWT se ejecuta ANTES del filtro de usuario/contraseña
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para encriptar contraseñas con BCrypt.
    // BCrypt es el estándar: aplica un hash adaptativo con salt aleatorio.
    // Nunca se guarda la contraseña en texto plano.
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para el AuthenticationManager. Valida usuario+contraseña durante el login
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}