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

    // 5 CONSULTAS PERSONALIZADAS CON @Query

    // QUERY 1: findByClienteIdAndTotalMinimo debe retornar solo los pedidos del cliente
    // indicado cuyo total sea mayor o igual al mínimo. El pedido de 42000 no debe aparecer.
    @Test
    void pedidoRepository_findByClienteIdAndTotalMinimo_filtraCorrectamente() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado",  75000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 100000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  60000.0, cliente2));

        // Act
        List<Pedido> resultado = pedidoRepository.findByClienteIdAndTotalMinimo(cliente1.getId(), 50000.0);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(p -> {
            assertEquals(cliente1.getId(), p.getCliente().getId());
            assertTrue(p.getTotal() >= 50000.0);
        });
    }

    // QUERY 2: findByEstadoIn debe retornar pedidos cuyo estado esté dentro de la lista.
    // Los de estado "Cancelado" no deben aparecer.
    @Test
    void pedidoRepository_findByEstadoIn_retornaSoloPedidosConEstadosIndicados() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",      42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "En preparación", 55000.0, cliente2));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Cancelado",      30000.0, cliente1));

        // Act
        List<Pedido> resultado = pedidoRepository.findByEstadoIn(
                List.of("Pendiente", "En preparación"));

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(p ->
                assertNotEquals("Cancelado", p.getEstado()));
    }

    // QUERY 3: countPedidosByClienteId debe contar exactamente los pedidos del cliente indicado
    @Test
    void pedidoRepository_countPedidosByClienteId_retornaCantidadCorrecta() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 35000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 58000.0, cliente2));

        // Act
        long conteo = pedidoRepository.countPedidosByClienteId(cliente1.getId());

        // Assert
        assertEquals(2L, conteo);
    }

    // QUERY 4 - findPedidosConTotalMayorQue retorna solo los pedidos que superen el total
    // indicado, ordenados de mayor a menor
    @Test
    void pedidoRepository_findPedidosConTotalMayorQue_retornaOrdenados() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  30000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado",  80000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 120000.0, cliente2));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Cancelado",  50000.0, cliente2));

        // Act
        List<Pedido> resultado = pedidoRepository.findPedidosConTotalMayorQue(60000.0);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(p -> assertTrue(p.getTotal() > 60000.0));
        // Verificar orden descendente
        assertTrue(resultado.get(0).getTotal() >= resultado.get(1).getTotal());
    }

    //QUERY 5 - findPedidosActivosByClienteId retorna solo los pedidos activos
    // (no Entregado ni Cancelado) del cliente indicado
    @Test
    void pedidoRepository_findPedidosActivosByClienteId_excluyeEstadosFinales() {
        // Arrange
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "En camino", 55000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 35000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Cancelado", 20000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 60000.0, cliente2));

        // Act
        List<Pedido> resultado = pedidoRepository.findPedidosActivosByClienteId(cliente1.getId());

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(p -> {
            assertEquals(cliente1.getId(), p.getCliente().getId());
            assertNotEquals("Entregado", p.getEstado());
            assertNotEquals("Cancelado", p.getEstado());
        });
    }
}