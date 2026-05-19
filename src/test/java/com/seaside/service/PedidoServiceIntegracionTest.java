package com.seaside.service;

import com.seaside.dto.PedidoRequest;
import com.seaside.model.*;
import com.seaside.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Pruebas de INTEGRACIÓN del servicio PedidoService.
// @SpringBootTest levanta toda la aplicación
// @DirtiesContext reinicia el contexto de Spring entre pruebas

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PedidoServiceIntegracionTest {

    // Servicio a probar
    @Autowired
    private PedidoService pedidoService;

    // Repositorios auxiliares para preparar el estado inicial
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private AdicionalesRepository adicionalesRepository;

    // Datos de prueba reutilizables
    private Cliente cliente1;
    private Cliente cliente2;
    private Producto producto1;
    private Adicionales adicional1;

    @BeforeEach
    void setUp() {
        Categoria categoria = categoriaRepository.save(new Categoria("Platos Fuertes"));

        cliente1 = clienteRepository.save(new Cliente(
                "María", "Test", "maria.integ@email.com",
                "pass1", "3001110001", "Cra 1 #1-1",
                new Carrito(LocalDateTime.now())));

        cliente2 = clienteRepository.save(new Cliente(
                "Pedro", "Test", "pedro.integ@email.com",
                "pass2", "3002220002", "Cra 2 #2-2",
                new Carrito(LocalDateTime.now())));

        producto1 = productoRepository.save(new Producto(
                "Ceviche Integración", "Desc", 40000.0, categoria,
                "https://img.com/ceviche.jpg", 30, false, null));

        adicional1 = adicionalesRepository.save(new Adicionales(
                "Limones Test", "3 limones", 2000.0,
                "https://img.com/limon.jpg", 2, false, categoria));
    }

    //  PRUEBAS DE INTEGRACIÓN

    // INT 1: crearPedido con un item válido debe persistir el pedido y devolver el objeto con ID asignado
    @Test
    void pedidoService_crearPedido_pedidoBasicoValido_seGuardaConId() {
        // Arrange
        PedidoRequest request = buildRequest(cliente1.getId(),
                producto1.getId(), 2, null);

        // Act
        Pedido resultado = pedidoService.crearPedido(request);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Pendiente", resultado.getEstado());
        // total = 40000 * 2 = 80000
        assertEquals(80000.0, resultado.getTotal(), 0.01);
    }

    // INT 2: crearPedido con adicionales debe sumar correctamente el total
    @Test
    void pedidoService_crearPedido_conAdicionales_calculaTotalCorrecto() {
        // Arrange - 1 producto x2 + 1 adicional x3
        PedidoRequest.AdicionalRequest adicionalReq = new PedidoRequest.AdicionalRequest();
        adicionalReq.setAdicionalId(adicional1.getId());
        adicionalReq.setCantidad(3);

        PedidoRequest request = buildRequest(cliente1.getId(),
                producto1.getId(), 2, List.of(adicionalReq));

        // Act
        Pedido resultado = pedidoService.crearPedido(request);

        // Assert - 40000*2 + 2000*3 = 80000 + 6000 = 86000
        assertNotNull(resultado);
        assertEquals(86000.0, resultado.getTotal(), 0.01);
    }

    // INT 3: crearPedido con clienteId inexistente debe lanzar excepción
    @Test
    void pedidoService_crearPedido_clienteInexistente_lanzaExcepcion() {
        // Arrange
        PedidoRequest request = buildRequest(9999, producto1.getId(), 1, null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> pedidoService.crearPedido(request));
    }

    // INT 4: findByClienteId debe retornar únicamente los pedidos del cliente solicitado
    @Test
    void pedidoService_findByClienteId_retornaSoloPedidosDelCliente() {
        // Arrange
        PedidoRequest req1 = buildRequest(cliente1.getId(), producto1.getId(), 1, null);
        PedidoRequest req2 = buildRequest(cliente1.getId(), producto1.getId(), 2, null);
        PedidoRequest req3 = buildRequest(cliente2.getId(), producto1.getId(), 1, null);

        pedidoService.crearPedido(req1);
        pedidoService.crearPedido(req2);
        pedidoService.crearPedido(req3);

        // Act
        List<Pedido> pedidosCliente1 = pedidoService.findByClienteId(cliente1.getId());

        // Assert
        assertNotNull(pedidosCliente1);
        assertEquals(2, pedidosCliente1.size());
        pedidosCliente1.forEach(p ->
                assertEquals(cliente1.getId(), p.getCliente().getId()));
    }

    // INT 5: actualizarEstado debe cambiar el estado del pedido en BD
    @Test
    void pedidoService_actualizarEstado_cambiaEstadoEnBaseDeDatos() {
        // Arrange
        Pedido pedido = pedidoService.crearPedido(
                buildRequest(cliente1.getId(), producto1.getId(), 1, null));

        // Act
        pedidoService.actualizarEstado(pedido.getId(), "EN_PREPARACION");
        Optional<Pedido> encontrado = pedidoService.findById(pedido.getId());

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals("EN_PREPARACION", encontrado.get().getEstado());
    }

    //INT 6: delete debe eliminar el pedido de la base de datos
    @Test
    void pedidoService_delete_eliminaPedidoCorrectamente() {
        // Arrange
        Pedido pedido = pedidoService.crearPedido(
                buildRequest(cliente1.getId(), producto1.getId(), 1, null));
        Integer id = pedido.getId();

        // Act
        pedidoService.delete(id);
        Optional<Pedido> resultado = pedidoService.findById(id);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // INT 7: findActivos excluye pedidos con estado "Entregado" o "Cancelado"
    @Test
    void pedidoService_findActivos_excluyeEstadosFinales() {
        // Arrange
        Pedido p1 = pedidoService.crearPedido(
                buildRequest(cliente1.getId(), producto1.getId(), 1, null));
        Pedido p2 = pedidoService.crearPedido(
                buildRequest(cliente2.getId(), producto1.getId(), 1, null));

        pedidoService.actualizarEstado(p1.getId(), "Entregado");
        // p2 queda en "Pendiente"

        // Act
        List<Pedido> activos = pedidoService.findActivos();

        // Assert
        assertNotNull(activos);
        // Solo p2 debería aparecer
        activos.forEach(p ->
                assertNotEquals("Entregado", p.getEstado()));
    }

    // Método auxiliar para construir PedidoRequest
    private PedidoRequest buildRequest(Integer clienteId, Integer productoId,
                                       int cantidad,
                                       List<PedidoRequest.AdicionalRequest> adicionales) {
        PedidoRequest req = new PedidoRequest();
        req.setClienteId(clienteId);

        PedidoRequest.ItemRequest item = new PedidoRequest.ItemRequest();
        item.setProductoId(productoId);
        item.setCantidad(cantidad);
        item.setAdicionales(adicionales != null ? adicionales : List.of());

        req.setItems(List.of(item));
        return req;
    }
}