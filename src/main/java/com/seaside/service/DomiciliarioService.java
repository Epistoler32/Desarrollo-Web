package com.seaside.service;

import com.seaside.model.Domiciliario;

import java.util.List;
import java.util.Optional;

public interface DomiciliarioService {
    List<Domiciliario> findAll();
    List<Domiciliario> findDisponibles();
    Optional<Domiciliario> findById(Integer id);
    Domiciliario save(Domiciliario domiciliario);
    void setDisponibilidad(Integer id, boolean disponible);
    void delete(Integer id);
}