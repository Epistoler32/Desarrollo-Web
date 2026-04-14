// ── PedidoRepository.java ─────────────────────────────────────────────────────
package com.seaside.repository;
 
import com.seaside.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
 
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByClienteId(Integer clienteId);
    List<Pedido> findByEstado(String estado);
}