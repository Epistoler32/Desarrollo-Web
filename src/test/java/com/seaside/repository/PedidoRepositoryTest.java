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
// @DataJpaTest crea una base de datos H2 en memoria solo para pruebas
// @ActiveProfiles("test") evita que DataLoader inserte datos reales

@DataJpaTest
@ActiveProfiles("test")
public class PedidoRepositoryTest {

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

    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    void setUp() {
        categoriaRepository.save(new Categoria("Platos Fuertes"));

        cliente1 = clienteRepository.save(new Cliente(
                "Ana", "Gómez", "ana.test@email.com",
                "1234", "1111111111", "Calle 1 #1-1",
                new Carrito(LocalDateTime.now())));

        cliente2 = clienteRepository.save(new Cliente(
                "Luis", "Torres", "luis.test@email.com",
                "1234", "2222222222", "Calle 2 #2-2",
                new Carrito(LocalDateTime.now())));
    }


    // CRUD 1 Guardar un pedido nuevo retorna el pedido con ID asignado
    @Test
    void pedidoRepository_save_retornaPedidoGuardado() {
        Pedido pedido = new Pedido(
                LocalDate.now(), LocalDate.now().plusDays(1),
                "Pendiente", 77000.0, cliente1);

        Pedido resultado = pedidoRepository.save(pedido);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Pendiente", resultado.getEstado());
        assertEquals(77000.0, resultado.getTotal());
    }

    // CRUD 2 findAll retorna exactamente los pedidos guardados
    @Test
    void pedidoRepository_findAll_retornaListaNoVacia() {
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 35000.0, cliente2));

        List<Pedido> lista = pedidoRepository.findAll();

        assertNotNull(lista);
        assertEquals(2, lista.size());
    }

    // CRUD 3 findById con ID inexistente retorna optional vacio
    @Test
    void pedidoRepository_findById_idInexistente_retornaVacio() {
        Optional<Pedido> resultado = pedidoRepository.findById(-99);

        assertTrue(resultado.isEmpty());
    }

    // CRUD 4 eliminar un pedido debe hacer que ya no sea encontrable
    @Test
    void pedidoRepository_delete_pedidoYaNoExiste() {
        Pedido pedido = pedidoRepository.save(new Pedido(
                LocalDate.now(), LocalDate.now(), "Cancelado", 50000.0, cliente1));
        Integer id = pedido.getId();

        pedidoRepository.deleteById(id);

        assertTrue(pedidoRepository.findById(id).isEmpty());
    }

    // CRUD 5 actualizar el estado de un pedido debe persistirse correctamente
    @Test
    void pedidoRepository_update_actualizaEstadoCorrectamente() {
        Pedido pedido = pedidoRepository.save(new Pedido(
                LocalDate.now(), LocalDate.now(), "Pendiente", 60000.0, cliente1));

        pedido.setEstado("EN_PREPARACION");
        Pedido actualizado = pedidoRepository.save(pedido);

        assertEquals("EN_PREPARACION", actualizado.getEstado());
    }

    // 5 consultas @Query 

    // QUERY 1: findByEstadoIn debe retornar pedidos cuyo estado este en la lista
    // El pedido con estado cancelado no debe aparecer
    @Test
    void pedidoRepository_findByEstadoIn_retornaSoloPedidosConEstadosIndicados() {
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",      42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "En preparación", 55000.0, cliente2));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Cancelado",      30000.0, cliente1));

        List<Pedido> resultado = pedidoRepository.findByEstadoIn(
                List.of("Pendiente", "En preparación"));

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(p -> assertNotEquals("Cancelado", p.getEstado()));
    }

    // QUERY 2 countPedidosByClienteId debe contar exactamente los pedidos del cliente indicado
    // El pedido de cliente2 no debe contarse
    @Test
    void pedidoRepository_countPedidosByClienteId_retornaCantidadCorrecta() {
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 35000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 58000.0, cliente2));

        long conteo = pedidoRepository.countPedidosByClienteId(cliente1.getId());

        assertEquals(2L, conteo);
    }

    // QUERY 3 findPedidosConTotalMayorQue debe retornar solo los pedidos que superen
    // el total indicado, ordenados de mayor a menor
    @Test
    void pedidoRepository_findPedidosConTotalMayorQue_retornaOrdenados() {
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",   30000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado",   80000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  120000.0, cliente2));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Cancelado",   50000.0, cliente2));

        List<Pedido> resultado = pedidoRepository.findPedidosConTotalMayorQue(60000.0);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(p -> assertTrue(p.getTotal() > 60000.0));
        // Verificar orden descendente
        assertTrue(resultado.get(0).getTotal() >= resultado.get(1).getTotal());
    }

    // QUERY 4 findPedidosActivosByClienteId debe retornar solo los pedidos activos del cliente indicado, excluyendo Entregado y Cancelado
    @Test
    void pedidoRepository_findPedidosActivosByClienteId_excluyeEstadosFinales() {
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 42000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "En camino", 55000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado", 35000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Cancelado", 20000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 60000.0, cliente2));

        List<Pedido> resultado = pedidoRepository.findPedidosActivosByClienteId(cliente1.getId());

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(p -> {
            assertEquals(cliente1.getId(), p.getCliente().getId());
            assertNotEquals("Entregado", p.getEstado());
            assertNotEquals("Cancelado", p.getEstado());
        });
    }

    // QUERY 5 findPedidosByClienteIdOrderByTotalDesc debe retornar los pedidos de un cliente ordenados de mayor a menor total
    @Test
    void pedidoRepository_findPedidosByClienteIdOrderByTotalDesc_retornaOrdenadosDescendente() {
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  30000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Entregado",  90000.0, cliente1));
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente",  60000.0, cliente1));
        // Este pedido es de cliente2 y no debe aparecer
        pedidoRepository.save(new Pedido(LocalDate.now(), LocalDate.now(), "Pendiente", 200000.0, cliente2));

        List<Pedido> resultado = pedidoRepository.findPedidosByClienteIdOrderByTotalDesc(cliente1.getId());

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        resultado.forEach(p -> assertEquals(cliente1.getId(), p.getCliente().getId()));
        // Verificar orden descendente
        assertEquals(90000.0, resultado.get(0).getTotal(), 0.01);
        assertEquals(60000.0, resultado.get(1).getTotal(), 0.01);
        assertEquals(30000.0, resultado.get(2).getTotal(), 0.01);
    }
}