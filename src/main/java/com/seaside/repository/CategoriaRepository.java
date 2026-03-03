package com.seaside.repository;
import com.seaside.model.Categoria;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CategoriaRepository {
    private Map<Integer, Categoria> categorias = new HashMap<>();

    public CategoriaRepository() {
        categorias.put(1, new Categoria(1, "platos_fuertes"));
        categorias.put(2, new Categoria(2, "adicionales"));
    };

    public List<Categoria> findAll() {
        return categorias.values().stream().toList();
    }

    public Categoria findById(Integer id) {
        return categorias.get(id);  
    }

}
