// ── AdministradorRepository.java ─────────────────────────────────────────────
package com.seaside.repository;

import com.seaside.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Administrador.
 * Usado exclusivamente por AdministradorServiceImpl para autenticar admins.
 */
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    /** Busca un administrador por correo (para autenticación). */
    Optional<Administrador> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}