package com.seaside.service;

import com.seaside.model.Operador;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestión de operadores.
 * Define operaciones de CRUD y autenticación de operadores.
 */
public interface OperadorService {
    /** Devuelve todos los operadores registrados. */
    List<Operador> findAll();

    /** Busca un operador por su id. */
    Optional<Operador> findById(Integer id);

    /** Guarda o actualiza un operador. */
    Operador save(Operador operador);

    /** Elimina un operador por su id. */
    void delete(Integer id);

    /** Autentica un operador por usuario y contraseña. */
    Optional<Operador> autenticar(String usuario, String contrasena);
}