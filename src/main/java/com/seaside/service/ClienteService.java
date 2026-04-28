package com.seaside.service;

import com.seaside.model.Cliente;
import java.util.Collection;
import java.util.Optional;

/**
 * Contrato de servicio para la gestión de clientes.
 * Define las operaciones de autenticación, registro y CRUD.
 */
public interface ClienteService {
    /** Busca un cliente por su id; lanza excepción si no existe. */
    Cliente buscarPorId(Integer id);

    /** Busca un cliente por su correo electrónico. */
    Optional<Cliente> buscarPorCorreo(String correo);

    /** Verifica credenciales: devuelve el cliente si son correctas. */
    Optional<Cliente> autenticar(String correo, String contrasena);

    /** Comprueba si ya existe una cuenta con ese correo. */
    boolean existeCorreo(String correo);

    // Registra un cliente nuevo garantizando que siempre tenga un carrito asociado.

    Cliente registrarNuevo(Cliente cliente);

    // Persiste un cliente ya construido tal cual (usado internamente o en
    // DataLoader).

    Cliente registrar(Cliente cliente);

    /** Actualiza los datos del cliente; preserva la contraseña si no se envía. */
    Cliente actualizar(Cliente cliente);

    /** Elimina un cliente por su id. */
    void eliminar(Integer id);

    /** Devuelve todos los clientes registrados. */
    Collection<Cliente> obtenerTodos();
}