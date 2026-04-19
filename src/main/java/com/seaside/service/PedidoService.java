package com.seaside.service;

import com.seaside.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> findAll();
    List<Pedido> findActivos();
    Optional<Pedido> findById(Integer id);
    Pedido save(Pedido pedido);
    void actualizarEstado(Integer id, String estado);
    void asignarDomiciliario(Integer pedidoId, Integer domiciliarioId);
    void delete(Integer id);

    Pedido crearPedido(com.seaside.dto.PedidoRequest request);
}