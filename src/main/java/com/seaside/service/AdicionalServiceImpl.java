package com.seaside.service;

import com.seaside.model.Adicionales;
import com.seaside.repository.AdicionalesRepository;
import com.seaside.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de AdicionalService.
 * Resuelve la entidad Categoria a una referencia gestionada por JPA antes de
 * guardar
 * para evitar la excepción TransientPropertyValueException de Hibernate.
 */
@Service
public class AdicionalServiceImpl implements AdicionalService {

    @Autowired
    private AdicionalesRepository adicionalesRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Adicionales> findAll() {
        return adicionalesRepository.findAll();
    }

    @Override
    public List<Adicionales> findByCategoria(Integer categoriaId) {
        return adicionalesRepository.findByCategoria_Id(categoriaId);
    }

    @Override
    public Adicionales findById(Integer id) {
        return adicionalesRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Adicionales save(Adicionales adicional) {
        // Resolve the Categoria to a managed JPA reference so Hibernate
        // doesn't treat it as a transient entity and throw an error.
        if (adicional.getCategoria() != null && adicional.getCategoria().getId() != null) {
            adicional.setCategoria(
                    categoriaRepository.getReferenceById(adicional.getCategoria().getId()));
        }
        return adicionalesRepository.save(adicional);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        adicionalesRepository.deleteById(id);
    }
}