package com.seaside.service;

import com.seaside.model.Categoria;
import com.seaside.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    // Orden del menu
    private static final List<String> ORDEN_MENU = List.of(
            "Entradas", "Platos Fuertes", "Acompañamientos", "Postres", "Bebidas"
    );

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Collection<Categoria> getAllCategories() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria searchById(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    // Devuelve las categorías ordenadas según ORDEN_MENU.
    @Override
    public List<Categoria> getAllCategoriesOrdenadas() {
        return categoriaRepository.findAll().stream()
                .sorted(Comparator.comparingInt(c -> {
                    int idx = ORDEN_MENU.indexOf(c.getNombre());
                    return idx == -1 ? Integer.MAX_VALUE : idx;
                }))
                .collect(Collectors.toList());
    }
}