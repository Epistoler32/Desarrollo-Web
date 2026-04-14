// ── DomiciliarioRepository.java ───────────────────────────────────────────────
package com.seaside.repository;
 
import com.seaside.model.Domiciliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
 
@Repository
public interface DomiciliarioRepository extends JpaRepository<Domiciliario, Integer> {
    Optional<Domiciliario> findByCorreo(String correo);
    List<Domiciliario> findByDisponibleTrue();
    List<Domiciliario> findByActivoTrue();
}