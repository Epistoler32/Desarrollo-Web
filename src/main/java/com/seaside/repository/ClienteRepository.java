package com.seaside.repository;

import com.seaside.model.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Cliente.
 * Proporciona búsqueda por correo y verificación de unicidad.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    /** Busca un cliente por su dirección de correo electrónico. */
    Optional<Cliente> findByCorreo(String correo);

    /** Verifica si ya existe un cliente con ese correo. */
    boolean existsByCorreo(String correo);

}