package com.seaside.repository;

import com.seaside.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;

/**
 * Repositorio JPA para la entidad Producto.
 * Permite filtrar productos por el nombre de su categoría.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
        /** Busca productos cuya categoría tenga el nombre indicado. */
        Collection<Producto> findByCategoria_Nombre(String nombre);

}
