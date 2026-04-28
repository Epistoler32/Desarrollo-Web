package com.seaside.service;

import com.seaside.model.Operador;

import java.util.List;
import java.util.Optional;

public interface OperadorService {
    List<Operador> findAll();

    Optional<Operador> findById(Integer id);

    Operador save(Operador operador);

    void delete(Integer id);

    /** Autentica un operador por usuario y contraseña. */
    Optional<Operador> autenticar(String usuario, String contrasena);
}