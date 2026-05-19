package com.seaside.repository;

import com.seaside.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio JPA para la entidad UserEntity
// Lo usa CustomUserDetailsService para cargar usuarios durante la autenticación

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    // Busca un usuario por username (correo o nombre de usuario)
    Optional<UserEntity> findByUsername(String username);

    // Verifica si ya existe un usuario con ese username
    boolean existsByUsername(String username);
}