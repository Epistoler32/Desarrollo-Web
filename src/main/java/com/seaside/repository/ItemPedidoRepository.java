// ── ItemPedidoRepository.java ─────────────────────────────────────────────────
package com.seaside.repository;
 
import com.seaside.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
 
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
    List<ItemPedido> findByPedidoId(Integer pedidoId);
    void deleteByPedidoId(Integer pedidoId);
}