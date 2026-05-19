package com.seaside.repository;

import com.seaside.model.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Operador.
 * Proporciona búsqueda por credenciales para el login de operadores.
 */
@Repository
public interface OperadorRepository extends JpaRepository<Operador, Integer> {
    Optional<Operador> findByUsuario(String usuario);
}
