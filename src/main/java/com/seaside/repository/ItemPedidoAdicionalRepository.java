package com.seaside.repository;

import com.seaside.model.ItemPedidoAdicional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoAdicionalRepository extends JpaRepository<ItemPedidoAdicional, Integer> {

    List<ItemPedidoAdicional> findByItemPedidoId(Integer itemPedidoId);

    void deleteByItemPedidoId(Integer itemPedidoId);
}