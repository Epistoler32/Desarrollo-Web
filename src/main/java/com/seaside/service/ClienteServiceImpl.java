package com.seaside.service;

import com.seaside.model.Cliente;
import com.seaside.repository.ClienteRepository;

import errors.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }

    @Override
    public boolean existeCorreo(String correo) {
        return clienteRepository.existeCorreo(correo);
    }

    @Override
    public Cliente registrar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Cliente cliente) {
        return clienteRepository.update(cliente);
    }

    @Override
    public void eliminar(Integer id) {
        clienteRepository.delete(id);
    }

    @Override
    public Collection<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }
}