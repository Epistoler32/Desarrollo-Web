package com.seaside.service;

import com.seaside.model.Domiciliario;
import com.seaside.repository.DomiciliarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DomiciliarioServiceImpl implements DomiciliarioService {

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    @Override
    public List<Domiciliario> findAll() {
        return domiciliarioRepository.findAll();
    }

    @Override
    public List<Domiciliario> findDisponibles() {
        return domiciliarioRepository.findByDisponibleTrue();
    }

    @Override
    public Optional<Domiciliario> findById(Integer id) {
        return domiciliarioRepository.findById(id);
    }

    @Override
    public Domiciliario save(Domiciliario domiciliario) {
        return domiciliarioRepository.save(domiciliario);
    }

    @Override
    @Transactional
    public void setDisponibilidad(Integer id, boolean disponible) {
        domiciliarioRepository.findById(id).ifPresent(d -> {
            d.setDisponible(disponible);
            domiciliarioRepository.save(d);
        });
    }

    @Override
    @Transactional
    public void setActivo(Integer id, boolean activo) {
        domiciliarioRepository.findById(id).ifPresent(d -> {
            d.setActivo(activo);
            domiciliarioRepository.save(d);
        });
    }

    @Override
    public void delete(Integer id) {
        domiciliarioRepository.deleteById(id);
    }
}