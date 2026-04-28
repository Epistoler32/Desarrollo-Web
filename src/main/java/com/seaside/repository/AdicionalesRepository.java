// ── AdicionalesRepository.java ────────────────────────────────────────────────
package com.seaside.repository;

import com.seaside.model.Adicionales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad Adicionales.
 * Permite filtrar adicionales por categoría (id o nombre).
 */
@Repository
public interface AdicionalesRepository extends JpaRepository<Adicionales, Integer> {
    // Buscar adicionales por categoría
    List<Adicionales> findByCategoria_Id(Integer categoriaId);

    List<Adicionales> findByCategoria_Nombre(String nombre);
}