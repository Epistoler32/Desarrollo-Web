package com.seaside.service;

import com.seaside.model.ItemPedido;
import com.seaside.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> findAll();

    List<Pedido> findActivos();

    /** Retorna pedidos cuyo clienteId coincide */
    List<Pedido> findByClienteId(Integer clienteId);

    Optional<Pedido> findById(Integer id);

    Pedido save(Pedido pedido);

    void actualizarEstado(Integer id, String estado);

    void asignarDomiciliario(Integer pedidoId, Integer domiciliarioId);

    void delete(Integer id);

    /** Retorna los ítems (líneas) que pertenecen al pedido indicado */
    List<ItemPedido> getItemsByPedidoId(Integer pedidoId);

    Pedido crearPedido(com.seaside.dto.PedidoRequest request);
}