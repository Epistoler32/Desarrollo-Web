package com.seaside.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/// Configuración CORS (Cross-Origin Resource Sharing)


/*
Se configura CORS aquí como un Bean de Spring con @Order(Ordered.HIGHEST_PRECEDENCE)
para que se ejecute ANTES que el filtro de seguridad.

Permite que el frontend Angular (http://localhost:4200) consuma la API REST
*/

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos: solo nuestro frontend Angular
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // Headers permitidos en las peticiones del frontend
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // Permite enviar cookies y el header Authorization
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}