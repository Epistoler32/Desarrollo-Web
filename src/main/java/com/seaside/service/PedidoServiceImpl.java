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

    private static final List<String> ESTADOS_INACTIVOS =
            Arrays.asList("Entregado", "Cancelado", "ENTREGADO", "CANCELADO");

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    private void populateDomiciliarioId(Pedido pedido) {
        domiciliarioRepository.findByPedidoId(pedido.getId())
                .ifPresent(d -> pedido.setDomiciliarioId(d.getId()));
    }

    @Override
    public List<Pedido> findAll() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        pedidos.forEach(this::populateDomiciliarioId);
        return pedidos;
    }

    @Override
    public List<Pedido> findActivos() {
        List<Pedido> pedidos = pedidoRepository.findAll().stream()
                .filter(p -> !ESTADOS_INACTIVOS.contains(p.getEstado()))
                .toList();
        pedidos.forEach(this::populateDomiciliarioId);
        return pedidos;
    }

    @Override
    public Optional<Pedido> findById(Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        pedido.ifPresent(this::populateDomiciliarioId);
        return pedido;
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

        // Si el pedido ya tenía un domiciliario distinto, liberarlo
        domiciliarioRepository.findByPedidoId(pedidoId).ifPresent(anterior -> {
            if (!anterior.getId().equals(domiciliarioId)) {
                anterior.setPedido(null);
                anterior.setDisponible(true);
                domiciliarioRepository.save(anterior);
            }
        });

        domiciliario.setPedido(pedido);
        domiciliario.setDisponible(false);
        domiciliarioRepository.save(domiciliario);

        pedido.setEstado("EN_CAMINO");
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // Liberar domiciliarios asignados antes de eliminar el pedido
        domiciliarioRepository.findAllByPedidoId(id).forEach(d -> {
            d.setPedido(null);
            d.setDisponible(true);
            domiciliarioRepository.save(d);
        });

        itemPedidoRepository.deleteByPedidoId(id);
        pedidoRepository.deleteById(id);
    }
}