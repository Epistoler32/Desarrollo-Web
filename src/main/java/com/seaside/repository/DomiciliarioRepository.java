package com.seaside.repository;

import com.seaside.model.Domiciliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Domiciliario.
 * Incluye consultas para filtrar por disponibilidad, estado activo
 * y para encontrar el domiciliario asignado a un pedido específico.
 */
@Repository
public interface DomiciliarioRepository extends JpaRepository<Domiciliario, Integer> {
    Optional<Domiciliario> findByCorreo(String correo);

    /** Devuelve los domiciliarios que están disponibles (sin pedido asignado). */
    List<Domiciliario> findByDisponibleTrue();

    /** Devuelve los domiciliarios que están activos (trabajando ese día). */
    List<Domiciliario> findByActivoTrue();

    // encontrar el domiciliario asignado a un pedido
    Optional<Domiciliario> findByPedidoId(Integer pedidoId);

    // limpiar la asignación cuando se elimina un pedido
    List<Domiciliario> findAllByPedidoId(Integer pedidoId);
}