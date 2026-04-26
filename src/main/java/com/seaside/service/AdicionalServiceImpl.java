package com.seaside.service;

import com.seaside.model.Adicionales;
import com.seaside.repository.AdicionalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdicionalServiceImpl implements AdicionalService {

    @Autowired
    private AdicionalesRepository adicionalesRepository;

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
        return adicionalesRepository.save(adicional);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        adicionalesRepository.deleteById(id);
    }
}