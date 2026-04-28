package com.seaside.repository;

import com.seaside.model.Operador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Operador.
 * Proporciona búsqueda por credenciales para el login de operadores.
 */
public interface OperadorRepository extends JpaRepository<Operador, Integer> {
    /** Busca un operador por usuario y contraseña (para autenticación). */
    Optional<Operador> findByUsuarioAndContrasena(String usuario, String contrasena);
}
