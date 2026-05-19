package com.seaside.repository;

import com.seaside.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


// Repositorio JPA para la entidad Role
// Permite buscar roles por nombre para asignarlos al registrar usuarios

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Busca un rol por su nombre (ej. "CLIENTE", "ADMINISTRADOR")
    Optional<Role> findByNombre(String nombre);
}