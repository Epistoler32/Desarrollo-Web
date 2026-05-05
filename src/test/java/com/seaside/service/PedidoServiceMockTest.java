package com.seaside.service;

import com.seaside.dto.PedidoRequest;
import com.seaside.model.*;
import com.seaside.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// Pruebas UNITARIAS con Mocks del servicio PedidoService
// Se usa Mockito para simular los repositorios
// @InjectMocks crea la instancia real del servicio a probar
// @Mock crea objetos falsos para todas las dependencias del servicio

@ExtendWith(MockitoExtension.class)
public class PedidoServiceMockTest {

    // Clase que se prueba (implementación real)
    @InjectMocks
    private PedidoServiceImpl pedidoService;

    // Mocks de repositorios (objetos falsos)
    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private AdicionalesRepository adicionalesRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ItemPedidoAdicionalRepository itemPedidoAdicionalRepository;

    @Mock
    private DomiciliarioRepository domiciliarioRepository;

    //  MOCK 1: findAll devuelve la lista que simulamos en el mock

    @Test
    void pedidoService_findAll_retornaListaMoqueada() {
        // Arrange
        Cliente clienteFalso = buildClienteFalso();
        Pedido p1 = new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  42000.0, clienteFalso);
        Pedido p2 = new Pedido(LocalDate.now(), LocalDate.now(), "Entregado",  35000.0, clienteFalso);
        p1.setId(1);
        p2.setId(2);

        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2));
        when(domiciliarioRepository.findByPedidoId(anyInt())).thenReturn(Optional.empty());

        // Act
        List<Pedido> resultado = pedidoService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        // Verificamos que el repositorio fue llamado exactamente una vez
        verify(pedidoRepository, times(1)).findAll();
    }

    //  MOCK 2: findById con ID existente retorna el pedido correcto

    @Test
    void pedidoService_findById_idExistente_retornaPedido() {
        // Arrange
        Cliente clienteFalso = buildClienteFalso();
        Pedido pedidoFalso = new Pedido(
                LocalDate.now(), LocalDate.now(), "Pendiente", 55000.0, clienteFalso);
        pedidoFalso.setId(10);

        when(pedidoRepository.findById(10)).thenReturn(Optional.of(pedidoFalso));
        when(domiciliarioRepository.findByPedidoId(10)).thenReturn(Optional.empty());

        // Act
        Optional<Pedido> resultado = pedidoService.findById(10);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(55000.0, resultado.get().getTotal());
    }

    //  MOCK 3: findById con ID inexistente retorna Optional vacío

    @Test
    void pedidoService_findById_idInexistente_retornaVacio() {
        // Arrange
        when(pedidoRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Pedido> resultado = pedidoService.findById(999);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    //  MOCK 4: findActivos filtra correctamente los estados inactivos

    @Test
    void pedidoService_findActivos_filtraEstadosFinales() {
        // Arrange
        Cliente clienteFalso = buildClienteFalso();
        Pedido activo    = new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",      42000.0, clienteFalso);
        Pedido entregado = new Pedido(LocalDate.now(), LocalDate.now(), "Entregado",      35000.0, clienteFalso);
        Pedido cancelado = new Pedido(LocalDate.now(), LocalDate.now(), "Cancelado",      20000.0, clienteFalso);
        activo.setId(1); entregado.setId(2); cancelado.setId(3);

        when(pedidoRepository.findAll()).thenReturn(List.of(activo, entregado, cancelado));
        when(domiciliarioRepository.findByPedidoId(1)).thenReturn(Optional.empty());

        // Act
        List<Pedido> activos = pedidoService.findActivos();

        // Assert
        assertNotNull(activos);
        assertEquals(1, activos.size());
        assertEquals("Pendiente", activos.get(0).getEstado());
    }

    //  MOCK 5: actualizarEstado cambia el estado sin tocar la BD real

    @Test
    void pedidoService_actualizarEstado_llamaRepositorioSaveConNuevoEstado() {
        // Arrange
        Cliente clienteFalso = buildClienteFalso();
        Pedido pedidoFalso = new Pedido(
                LocalDate.now(), LocalDate.now(), "Pendiente", 42000.0, clienteFalso);
        pedidoFalso.setId(5);

        when(pedidoRepository.findById(5)).thenReturn(Optional.of(pedidoFalso));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoFalso);
        lenient().when(domiciliarioRepository.findByPedidoId(5)).thenReturn(Optional.empty());

        // Act
        pedidoService.actualizarEstado(5, "EN_PREPARACION");

        // Assert - verificamos que save fue invocado (la BD falsa "procesó" el cambio)
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        assertEquals("EN_PREPARACION", pedidoFalso.getEstado());
    }

    //  MOCK 6: crearPedido con clienteId inválido lanza excepción

    @Test
    void pedidoService_crearPedido_clienteInexistente_lanzaExcepcion() {
        // Arrange - el mock devuelve vacío simulando que el cliente no existe
        when(clienteRepository.findById(9999)).thenReturn(Optional.empty());

        PedidoRequest request = new PedidoRequest();
        request.setClienteId(9999);
        request.setItems(List.of());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> pedidoService.crearPedido(request));
    }

    //  MOCK 7: crearPedido con datos válidos persiste el pedido mockeado

    @Test
    void pedidoService_crearPedido_datosValidos_retornaPedidoGuardado() {
        // Arrange
        Cliente clienteFalso = buildClienteFalso();
        Producto productoFalso = new Producto(
                "Ceviche Mock", "desc", 40000.0,
                new Categoria("Platos Fuertes"),
                "https://img.com/c.jpg", 30, false, null);
        productoFalso.setId(1);

        Pedido pedidoGuardado = new Pedido(
                LocalDate.now(), LocalDate.now(), "Pendiente", 80000.0, clienteFalso);
        pedidoGuardado.setId(100);

        when(clienteRepository.findById(1)).thenReturn(Optional.of(clienteFalso));
        when(productoRepository.findById(1)).thenReturn(Optional.of(productoFalso));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardado);

        PedidoRequest.ItemRequest itemReq = new PedidoRequest.ItemRequest();
        itemReq.setProductoId(1);
        itemReq.setCantidad(2);
        itemReq.setAdicionales(List.of());

        PedidoRequest request = new PedidoRequest();
        request.setClienteId(1);
        request.setItems(List.of(itemReq));

        // Act
        Pedido resultado = pedidoService.crearPedido(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(100, resultado.getId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    //  MOCK 8: delete llama a deleteById en el repositorio

    @Test
    void pedidoService_delete_invocaRepositorioDeleteById() {
        // Arrange
        when(domiciliarioRepository.findAllByPedidoId(7)).thenReturn(List.of());
        doNothing().when(itemPedidoRepository).deleteByPedidoId(7);
        doNothing().when(pedidoRepository).deleteById(7);

        // Act
        pedidoService.delete(7);

        // Assert - verificamos que se invocó el método de borrado
        verify(pedidoRepository, times(1)).deleteById(7);
    }

    // Método auxiliar para crear un cliente falso sin persistir
    private Cliente buildClienteFalso() {
        Cliente c = new Cliente(
                "Mock", "Cliente", "mock@test.com",
                "pass", "3000000000", "Calle Mock");
        c.setId(1);
        return c;
    }
}