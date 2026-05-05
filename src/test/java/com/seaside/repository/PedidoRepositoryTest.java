package com.seaside.repository;

import com.seaside.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// pruebas PedidoRepository

// @DataJpaTest crea una base de datos H2 para pruebas

// @ActiveProfiles("test") usa el perfil de pruebas definido en application.properties para no ejecutar DataLoader ni tocar datos reales

@DataJpaTest
@ActiveProfiles("test")
public class PedidoRepositoryTest {

    // Repositorios necesarios 
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    // Datos de prueba reutilizables
    private Cliente cliente1;
    private Cliente cliente2;
    private Producto producto1;
    private Producto producto2;

    // Inicializa un estado conocido y controlado en la base de datos H2.
    @BeforeEach
    void setUp() {
        // Categoría base
        Categoria platos = categoriaRepository.save(new Categoria("Platos Fuertes"));

        // Dos clientes de prueba (cada uno con su carrito)
        cliente1 = clienteRepository.save(new Cliente(
                "Ana", "Gómez", "ana.test@email.com",
                "1234", "1111111111", "Calle 1 #1-1",
                new Carrito(LocalDateTime.now())));

        cliente2 = clienteRepository.save(new Cliente(
                "Luis", "Torres", "luis.test@email.com",
                "1234", "2222222222", "Calle 2 #2-2",
                new Carrito(LocalDateTime.now())));

        // Dos productos de prueba
        producto1 = productoRepository.save(new Producto(
                "Ceviche Test", "Descripción ceviche", 42000.0, platos,
                "https://img.com/1.jpg", 30, true, "Pescado"));

        producto2 = productoRepository.save(new Producto(
                "Arroz Test", "Descripción arroz", 35000.0, platos,
                "https://img.com/2.jpg", 25, false, null));
    }

    //  PRUEBAS CRUD BÁSICAS - metodos heredados de JpaRepository
    // CRUD 1 - Guardar un pedido nuevo debe retornar el pedido con ID asignado
    @Test
    void pedidoRepository_save_retornaPedidoGuardado() {
        // Arrange
        Pedido pedido = new Pedido(
                LocalDate.now(), LocalDate.now().plusDays(1),
                "Pendiente", 77000.0, cliente1);

        // Act
        Pedido resultado = pedidoRepository.save(pedido);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Pendiente", resultado.getEstado());
        assertEquals(77000.0, resultado.getTotal());
    }

    //CRUD-2 - findAll debe retornar exactamente los pedidos guardados en setUp
    @Test
    void pedidoRepository_findAll_retornaListaNoVacia() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 35000.0, cliente2));

        // Act
        List<Pedido> lista = pedidoRepository.findAll();

        // Assert
        assertNotNull(lista);
        assertEquals(2, lista.size());
        assertTrue(lista.size() > 0);
    }

    //CRUD 3 - findById con un ID inexistente debe retornar Optional vacío
    @Test
    void pedidoRepository_findById_idInexistente_retornaVacio() {
        // Arrange
        Integer idInexistente = -99;

        // Act
        Optional<Pedido> resultado = pedidoRepository.findById(idInexistente);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // CRUD 4: Eliminar un pedido debe hacer que ya no sea encontrable
    @Test
    void pedidoRepository_delete_pedidoYaNoExiste() {
        // Arrange
        Pedido pedido = pedidoRepository.save(new Pedido(
                LocalDate.now(), LocalDate.now(), "Cancelado", 50000.0, cliente1));
        Integer idGuardado = pedido.getId();

        // Act
        pedidoRepository.deleteById(idGuardado);
        Optional<Pedido> resultado = pedidoRepository.findById(idGuardado);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // CRUD-5: Actualizar el estado de un pedido debe persistirse correctamente
    @Test
    void pedidoRepository_update_actualizaEstadoCorrectamente() {
        // Arrange
        Pedido pedido = pedidoRepository.save(new Pedido(
                LocalDate.now(), LocalDate.now(), "Pendiente", 60000.0, cliente1));

        // Act
        pedido.setEstado("EN_PREPARACION");
        Pedido actualizado = pedidoRepository.save(pedido);

        // Assert
        assertNotNull(actualizado);
        assertEquals("EN_PREPARACION", actualizado.getEstado());
    }

    // 5 CONSULTAS PERSONALIZADAS

    // QUERY 1: findByClienteId debe retornar solo los pedidos del cliente indicado
    @Test
    void pedidoRepository_findByClienteId_retornaUnicamente_pedidosDeEseCliente() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado",  35000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "EN_CAMINO",  58000.0, cliente2));

        // Act
        List<Pedido> pedidosCliente1 = pedidoRepository.findByClienteId(cliente1.getId());

        // Assert
        assertNotNull(pedidosCliente1);
        assertEquals(2, pedidosCliente1.size());
        pedidosCliente1.forEach(p ->
                assertEquals(cliente1.getId(), p.getCliente().getId()));
    }

    // QUERY 2: findByEstado debe retornar solo los pedidos con estado "Pendiente"
    @Test
    void pedidoRepository_findByEstado_retornarSoloPedidosPendientes() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  55000.0, cliente2));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado",  35000.0, cliente1));

        // Act
        List<Pedido> pendientes = pedidoRepository.findByEstado("Pendiente");

        // Assert
        assertNotNull(pendientes);
        assertEquals(2, pendientes.size());
        pendientes.forEach(p -> assertEquals("Pendiente", p.getEstado()));
    }

    // QUERY 3: findByClienteId para un cliente sin pedidos debe retornar lista vacía
    @Test
    void pedidoRepository_findByClienteId_clienteSinPedidos_retornaListaVacia() {
        // Arrange - cliente2 no tiene pedidos guardados

        // Act
        List<Pedido> resultado = pedidoRepository.findByClienteId(cliente2.getId());

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // QUERY 4 - findByEstado con estado "Entregado" retorna exactamente los entregados
    @Test
    void pedidoRepository_findByEstado_entregado_retornaListaCorrecta() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 35000.0, cliente2));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Cancelado", 60000.0, cliente1));

        // Act
        List<Pedido> entregados = pedidoRepository.findByEstado("Entregado");

        // Assert
        assertNotNull(entregados);
        assertEquals(2, entregados.size());
    }

    //QUERY 5 - Un pedido guardado con findById debe tener los mismos datos (total, estado, cliente) que cuando fue creado.
    @Test
    void pedidoRepository_findById_retornaPedidoConDatosCorrectos() {
        // Arrange
        Pedido pedidoOriginal = pedidoRepository.save(new Pedido(
                LocalDate.now(), LocalDate.now().plusDays(2),
                "EN_PREPARACION", 99000.0, cliente1));

        // Act
        Optional<Pedido> resultado = pedidoRepository.findById(pedidoOriginal.getId());

        // Assert
        assertTrue(resultado.isPresent());
        Pedido encontrado = resultado.get();
        assertEquals(99000.0,             encontrado.getTotal());
        assertEquals("EN_PREPARACION",    encontrado.getEstado());
        assertEquals(cliente1.getId(),    encontrado.getCliente().getId());
    }
}