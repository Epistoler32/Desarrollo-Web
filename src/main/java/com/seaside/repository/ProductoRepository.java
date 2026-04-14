package com.seaside.repository;

import com.seaside.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

        Collection<Producto> findByCategoria_Nombre(String nombre);
        
}
