package com.seaside.service;

import com.seaside.model.Domiciliario;
import com.seaside.model.ItemPedidoAdicional;
import com.seaside.model.Pedido;
import com.seaside.repository.DomiciliarioRepository;
import com.seaside.repository.ItemPedidoAdicionalRepository;
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

    private static final List<String> ESTADOS_INACTIVOS = Arrays.asList(
            "Entregado", "Cancelado", "ENTREGADO", "CANCELADO");

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private DomiciliarioRepository domiciliarioRepository;
    @Autowired private ItemPedidoRepository itemPedidoRepository;
    @Autowired private ItemPedidoAdicionalRepository itemPedidoAdicionalRepository;
    @Autowired private com.seaside.repository.ClienteRepository clienteRepository;
    @Autowired private com.seaside.repository.ProductoRepository productoRepository;
    @Autowired private com.seaside.repository.AdicionalesRepository adicionalesRepository;

    // ── Helpers ───────────────────────────────────────────────────────────

    private void populateDomiciliarioId(Pedido pedido) {
        domiciliarioRepository.findByPedidoId(pedido.getId())
                .ifPresent(d -> pedido.setDomiciliarioId(d.getId()));
    }

    // ── Queries ───────────────────────────────────────────────────────────

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

    // ── Mutations ─────────────────────────────────────────────────────────

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

        // Liberar domiciliario anterior si existía
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
        // Liberar domiciliarios antes de eliminar
        domiciliarioRepository.findAllByPedidoId(id).forEach(d -> {
            d.setPedido(null);
            d.setDisponible(true);
            domiciliarioRepository.save(d);
        });
        itemPedidoRepository.deleteByPedidoId(id);
        pedidoRepository.deleteById(id);
    }

    // ── Crear pedido ──────────────────────────────────────────────────────

    @Override
    @Transactional
    public Pedido crearPedido(com.seaside.dto.PedidoRequest request) {

        // 1. Validar cliente
        com.seaside.model.Cliente cliente = clienteRepository
                .findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente no encontrado: " + request.getClienteId()));

        // 2. Calcular total recorriendo items y sus adicionales
        double total = 0.0;
        for (com.seaside.dto.PedidoRequest.ItemRequest itemReq : request.getItems()) {

            com.seaside.model.Producto producto = productoRepository
                    .findById(itemReq.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + itemReq.getProductoId()));

            double subtotalProducto = producto.getPrecio() * itemReq.getCantidad();

            if (itemReq.getAdicionales() != null) {
                for (com.seaside.dto.PedidoRequest.AdicionalRequest ar : itemReq.getAdicionales()) {
                    com.seaside.model.Adicionales adicional = adicionalesRepository
                            .findById(ar.getAdicionalId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Adicional no encontrado: " + ar.getAdicionalId()));
                    subtotalProducto += adicional.getPrecio() * ar.getCantidad();
                }
            }

            total += subtotalProducto;
        }

        // 3. Persistir el pedido cabecera
        java.time.LocalDate hoy = java.time.LocalDate.now();
        com.seaside.model.Pedido pedido = new com.seaside.model.Pedido(
                hoy, hoy, "Pendiente", total, cliente);
        pedido = pedidoRepository.save(pedido);

        // 4. Persistir cada ItemPedido y sus ItemPedidoAdicional
        for (com.seaside.dto.PedidoRequest.ItemRequest itemReq : request.getItems()) {

            com.seaside.model.Producto producto = productoRepository
                    .findById(itemReq.getProductoId())
                    .orElseThrow();

            // Subtotal del item = precio_producto * cantidad + suma_adicionales
            double subtotalItem = producto.getPrecio() * itemReq.getCantidad();

            if (itemReq.getAdicionales() != null) {
                for (com.seaside.dto.PedidoRequest.AdicionalRequest ar : itemReq.getAdicionales()) {
                    com.seaside.model.Adicionales adicional = adicionalesRepository
                            .findById(ar.getAdicionalId())
                            .orElseThrow();
                    subtotalItem += adicional.getPrecio() * ar.getCantidad();
                }
            }

            com.seaside.model.ItemPedido itemPedido = new com.seaside.model.ItemPedido(
                    itemReq.getCantidad(), subtotalItem, pedido, producto);
            itemPedido = itemPedidoRepository.save(itemPedido);

            // 5. Persistir cada adicional del item
            if (itemReq.getAdicionales() != null) {
                for (com.seaside.dto.PedidoRequest.AdicionalRequest ar : itemReq.getAdicionales()) {

                    com.seaside.model.Adicionales adicional = adicionalesRepository
                            .findById(ar.getAdicionalId())
                            .orElseThrow();

                    double subtotalAdicional = adicional.getPrecio() * ar.getCantidad();

                    ItemPedidoAdicional ipa = new ItemPedidoAdicional(
                            ar.getCantidad(), subtotalAdicional, itemPedido, adicional);
                    itemPedidoAdicionalRepository.save(ipa);
                }
            }
        }

        return pedido;
    }
}