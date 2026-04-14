package com.seaside.service;

import com.seaside.model.Adicionales;
import java.util.List;

public interface AdicionalService {
    List<Adicionales> findByCategoria(Integer categoriaId);
    List<Adicionales> findAll();
    Adicionales findById(Integer id);
    Adicionales save(Adicionales adicional);
    void delete(Integer id);
}