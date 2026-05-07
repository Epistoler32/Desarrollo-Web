package com.seaside.repository;

import com.seaside.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Consulta derivada - Spring Data genera el SQL automáticamente a partir del nombre
    List<Pedido> findByClienteId(Integer clienteId);

    //  Consultas @Query con JPQL propio 

    // QUERY 1: Pedidos cuyo estado se encuentre dentro de una lista de estados
    @Query("SELECT p FROM Pedido p WHERE p.estado IN :estados")
    List<Pedido> findByEstadoIn(@Param("estados") List<String> estados);

    // QUERY 2: Cuenta cuántos pedidos tiene un cliente específico
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :clienteId")
    long countPedidosByClienteId(@Param("clienteId") Integer clienteId);

    // QUERY 3: Pedidos que superen un total dado, ordenados de mayor a menor
    @Query("SELECT p FROM Pedido p WHERE p.total > :total ORDER BY p.total DESC")
    List<Pedido> findPedidosConTotalMayorQue(@Param("total") double total);

    // QUERY 4: Pedidos activos de un cliente (excluye Entregado y Cancelado)
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId " +
           "AND p.estado NOT IN ('Entregado', 'Cancelado', 'ENTREGADO', 'CANCELADO')")
    List<Pedido> findPedidosActivosByClienteId(@Param("clienteId") Integer clienteId);

    // QUERY 5: Pedidos de un cliente ordenados de mayor a menor total
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.total DESC")
    List<Pedido> findPedidosByClienteIdOrderByTotalDesc(@Param("clienteId") Integer clienteId);
}