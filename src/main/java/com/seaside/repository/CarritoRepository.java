//  CarritoRepository.java 
package com.seaside.repository;
 
import com.seaside.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
}