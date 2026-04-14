package com.seaside.service;

import com.seaside.model.Categoria;
import java.util.Collection;
import java.util.List;

public interface CategoriaService {
    Collection<Categoria> getAllCategories();
    Categoria searchById(Integer id);

    List<Categoria> getAllCategoriesOrdenadas();
}