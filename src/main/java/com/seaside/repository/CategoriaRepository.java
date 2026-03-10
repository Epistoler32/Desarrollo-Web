package com.seaside.repository;

import com.seaside.model.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Collection<Categoria> findByNombre(String nombre);

}
