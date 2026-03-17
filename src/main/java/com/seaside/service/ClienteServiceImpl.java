package com.seaside.service;

import com.seaside.model.Cliente;
import com.seaside.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Optional;

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

    @Override
    public Cliente registrar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Cliente cliente) {
        clienteRepository.findById(cliente.getId()).ifPresent(existing -> {
            // Preservar contraseña si llega vacía
            if (cliente.getContrasena() == null || cliente.getContrasena().isEmpty()) {
                cliente.setContrasena(existing.getContrasena());
            }
            // Preservar carrito — Thymeleaf no lo incluye en el form de editar perfil
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