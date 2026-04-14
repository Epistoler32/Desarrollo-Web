package com.seaside.service;

import com.seaside.model.Domiciliario;
import com.seaside.model.Pedido;
import com.seaside.repository.DomiciliarioRepository;
import com.seaside.repository.ItemPedidoRepository;
import com.seaside.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    // Estados que se consideran "activos" (aún no finalizados)
    private static final List<String> ESTADOS_INACTIVOS =
            Arrays.asList("Entregado", "Cancelado", "ENTREGADO", "CANCELADO");

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> findActivos() {
        return pedidoRepository.findAll().stream()
                .filter(p -> !ESTADOS_INACTIVOS.contains(p.getEstado()))
                .toList();
    }

    @Override
    public Optional<Pedido> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void actualizarEstado(Integer id, String estado) {
        pedidoRepository.findById(id).ifPresent(p -> {
            p.setEstado(estado);
            pedidoRepository.save(p);
        });
    }

    @Override
    @Transactional
    public void asignarDomiciliario(Integer pedidoId, Integer domiciliarioId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));

        Domiciliario domiciliario = domiciliarioRepository.findById(domiciliarioId)
                .orElseThrow(() -> new IllegalArgumentException("Domiciliario no encontrado: " + domiciliarioId));

        // Asociar domiciliario al pedido y marcarlo no disponible
        domiciliario.setPedido(pedido);
        domiciliario.setDisponible(false);
        domiciliarioRepository.save(domiciliario);

        // Cambiar estado del pedido a EN_CAMINO
        pedido.setEstado("EN_CAMINO");
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // Limpiar items antes de borrar el pedido
        itemPedidoRepository.deleteByPedidoId(id);
        pedidoRepository.deleteById(id);
    }
}