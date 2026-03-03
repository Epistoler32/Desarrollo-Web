package com.seaside.service;

import com.seaside.model.Categoria;
import com.seaside.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Collection<Categoria> getAllCategories() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria searchById(Integer id) {
        return categoriaRepository.findById(id);
    }


}
