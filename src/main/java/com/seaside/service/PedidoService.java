package com.seaside.service;

import com.seaside.model.ItemPedido;
import com.seaside.model.Pedido;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestión de pedidos.
 * Centraliza la lógica de creación, consulta, asignación de domiciliarios
 * y cambio de estado de los pedidos.
 */
public interface PedidoService {
    /** Devuelve todos los pedidos registrados. */
    List<Pedido> findAll();

    /** Devuelve solo los pedidos activos (excluye Entregado y Cancelado). */
    List<Pedido> findActivos();

    /** Retorna pedidos cuyo clienteId coincide */
    List<Pedido> findByClienteId(Integer clienteId);

    /** Busca un pedido por su id. */
    Optional<Pedido> findById(Integer id);

    /** Guarda o actualiza un pedido. */
    Pedido save(Pedido pedido);

    /**
     * Actualiza el estado del pedido; libera el domiciliario si es estado final.
     */
    void actualizarEstado(Integer id, String estado);

    /** Asigna un domiciliario disponible a un pedido existente. */
    void asignarDomiciliario(Integer pedidoId, Integer domiciliarioId);

    /** Elimina un pedido por su id. */
    void delete(Integer id);

    /** Retorna los ítems (líneas) que pertenecen al pedido indicado */
    List<ItemPedido> getItemsByPedidoId(Integer pedidoId);

    /** Crea un pedido completo a partir del DTO recibido desde el frontend. */
    Pedido crearPedido(com.seaside.dto.PedidoRequest request);
}