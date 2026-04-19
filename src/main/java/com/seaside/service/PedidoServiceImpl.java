package com.seaside.service;

import com.seaside.model.Domiciliario;
import com.seaside.model.Pedido;
import com.seaside.repository.AdicionalesRepository;
import com.seaside.repository.ClienteRepository;
import com.seaside.repository.DomiciliarioRepository;
import com.seaside.repository.ItemPedidoRepository;
import com.seaside.repository.PedidoRepository;
import com.seaside.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    // Estados que se consideran activos (aún no finalizados)
    private static final List<String> ESTADOS_INACTIVOS = Arrays.asList("Entregado", "Cancelado", "ENTREGADO",
            "CANCELADO");

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired private ClienteRepository clienteRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private AdicionalesRepository adicionalesRepository;

    

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


    // Sprint 9

    @Override
    @Transactional
    public Pedido crearPedido(com.seaside.dto.PedidoRequest request) {

        // Buscar cliente
        com.seaside.model.Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente no encontrado: " + request.getClienteId()));

        // Calcular total
        double total = 0.0;
        for (com.seaside.dto.PedidoRequest.ItemRequest item : request.getItems()) {
            com.seaside.model.Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + item.getProductoId()));

            // subtotal del producto
            double subtotalProducto = producto.getPrecio() * item.getCantidad();

            // sumar adicionales si los hay
            if (item.getAdicionales() != null) {
                for (com.seaside.dto.PedidoRequest.AdicionalRequest ar : item.getAdicionales()) {
                    com.seaside.model.Adicionales adicional = adicionalesRepository
                            .findById(ar.getAdicionalId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Adicional no encontrado: " + ar.getAdicionalId()));
                    subtotalProducto += adicional.getPrecio() * ar.getCantidad();
                }
            }

            total += subtotalProducto;
        }

        // Crear el pedido con estado PENDIENTE
        java.time.LocalDate hoy = java.time.LocalDate.now();
        com.seaside.model.Pedido pedido = new com.seaside.model.Pedido(
                hoy,
                hoy, // fechaEntrega provisional = hoy
                "Pendiente",
                total,
                cliente);
        pedido = pedidoRepository.save(pedido);

        // Crear los items del pedido
        for (com.seaside.dto.PedidoRequest.ItemRequest item : request.getItems()) {
            com.seaside.model.Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow();

            double subtotal = producto.getPrecio() * item.getCantidad();

            if (item.getAdicionales() != null) {
                for (com.seaside.dto.PedidoRequest.AdicionalRequest ar : item.getAdicionales()) {
                    com.seaside.model.Adicionales adicional = adicionalesRepository
                            .findById(ar.getAdicionalId()).orElseThrow();
                    subtotal += adicional.getPrecio() * ar.getCantidad();
                }
            }

            com.seaside.model.ItemPedido itemPedido = new com.seaside.model.ItemPedido(
                    item.getCantidad(), subtotal, pedido, producto);
            itemPedidoRepository.save(itemPedido);
        }

        return pedido;
    }
}