package com.seaside.repository;

import com.seaside.model.CarritoProducto;
import com.seaside.model.CarritoProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, CarritoProductoId> {
    List<CarritoProducto> findByCarritoId(Integer carritoId);
    void deleteByCarritoId(Integer carritoId);

    // Necesario para poder borrar un producto que esté en carritos
    void deleteByProductoId(Integer productoId);
}