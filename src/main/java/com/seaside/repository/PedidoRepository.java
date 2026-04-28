// ── PedidoRepository.java ─────────────────────────────────────────────────────
package com.seaside.repository;

import com.seaside.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad Pedido.
 * Incluye consultas derivadas para filtrar por cliente y por estado.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    /** Devuelve todos los pedidos de un cliente específico. */
    List<Pedido> findByClienteId(Integer clienteId);

    /** Devuelve los pedidos que tienen un estado determinado. */
    List<Pedido> findByEstado(String estado);
}