package com.seaside.service;

import com.seaside.model.Cliente;
import java.util.Collection;
import java.util.Optional;

public interface ClienteService {
    Cliente buscarPorId(Integer id);
    Optional<Cliente> buscarPorCorreo(String correo);
    Optional<Cliente> autenticar(String correo, String contrasena);
    boolean existeCorreo(String correo);

    // Registra un cliente nuevo garantizando que siempre tenga un carrito asociado.

    Cliente registrarNuevo(Cliente cliente);

    // Persiste un cliente ya construido tal cual (usado internamente o en DataLoader).

    Cliente registrar(Cliente cliente);

    Cliente actualizar(Cliente cliente);
    void eliminar(Integer id);
    Collection<Cliente> obtenerTodos();
}