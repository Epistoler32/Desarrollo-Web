package com.seaside.service;

import com.seaside.model.Carrito;
import com.seaside.model.Cliente;
import com.seaside.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Implementación de ClienteService.
 * Gestiona el registro, autenticación y actualización de clientes,
 * garantizando que cada cliente nuevo tenga un carrito de compras asociado.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id).get();
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }

    @Override
    public Optional<Cliente> autenticar(String correo, String contrasena) {
        return clienteRepository.findByCorreo(correo)
                .filter(c -> c.getContrasena().equals(contrasena));
    }

    @Override
    public boolean existeCorreo(String correo) {
        return clienteRepository.existsByCorreo(correo);
    }

    // Registra un cliente nuevo garantizando que siempre tenga un carrito asociado.

    @Override
    public Cliente registrarNuevo(Cliente cliente) {
        if (cliente.getCarrito() == null) {
            cliente.setCarrito(new Carrito(LocalDateTime.now()));
        }
        return clienteRepository.save(cliente);
    }

    // Persiste el cliente tal cual, sin modificar su estado.
    @Override
    public Cliente registrar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Cliente cliente) {
        clienteRepository.findById(cliente.getId()).ifPresent(existing -> {
            if (cliente.getContrasena() == null || cliente.getContrasena().isEmpty()) {
                cliente.setContrasena(existing.getContrasena());
            }
            // el cliente no puede cambiar su carrito desde el perfil
            if (cliente.getCarrito() == null) {
                cliente.setCarrito(existing.getCarrito());
            }
        });
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Collection<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }
}