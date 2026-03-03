package com.seaside.service;

import com.seaside.model.Categoria;
import java.util.Collection;

public interface CategoriaService {
    Collection<Categoria> getAllCategories();
    Categoria searchById(Integer id);
}
