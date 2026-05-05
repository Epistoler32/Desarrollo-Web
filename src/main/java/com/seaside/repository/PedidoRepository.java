package com.seaside.repository;

import com.seaside.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Pedido.
 * Incluye consultas derivadas y consultas @Query con JPQL propio
 * para filtrar por cliente, estado y total mínimo.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Consultas derivadas (Spring genera el SQL automáticamente)

    // Devuelve todos los pedidos de un cliente específico
    List<Pedido> findByClienteId(Integer clienteId);

    // Devuelve los pedidos que tienen un estado determinado.
    List<Pedido> findByEstado(String estado);

    // Consultas @Query con JPQL propio

    // QUERY A: Busca pedidos de un cliente cuyo total sea mayor o igual al mínimo indicado
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.total >= :minTotal")
    List<Pedido> findByClienteIdAndTotalMinimo(
            @Param("clienteId") Integer clienteId,
            @Param("minTotal") double minTotal);

    // QUERY B: Devuelve pedidos cuyo estado se encuentre dentro de una lista de estados proporcionada
    @Query("SELECT p FROM Pedido p WHERE p.estado IN :estados")
    List<Pedido> findByEstadoIn(@Param("estados") List<String> estados);

    // QUERY C: Cuenta cuántos pedidos tiene un cliente específico
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :clienteId")
    long countPedidosByClienteId(@Param("clienteId") Integer clienteId);

    // QUERY D: Devuelve pedidos que superen un total dado, ordenados de mayor a menor precio
    @Query("SELECT p FROM Pedido p WHERE p.total > :total ORDER BY p.total DESC")
    List<Pedido> findPedidosConTotalMayorQue(@Param("total") double total);

    // QUERY E: Devuelve pedidos activos de un cliente concreto
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId " +
           "AND p.estado NOT IN ('Entregado', 'Cancelado', 'ENTREGADO', 'CANCELADO')")
    List<Pedido> findPedidosActivosByClienteId(@Param("clienteId") Integer clienteId);
}