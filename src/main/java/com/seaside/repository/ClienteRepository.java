package com.seaside.repository;

import com.seaside.model.Cliente;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ClienteRepository {

    private Map<Integer, Cliente> clientes = new HashMap<>();
    private Integer nextId = 4;

    public ClienteRepository() {
        // Datos de prueba
        clientes.put(1, new Cliente(1, "María", "Pérez",
                "maria@email.com", "1234", "321000001", "Calle 1 #10-20"));

        clientes.put(2, new Cliente(2, "Juan", "Rodríguez",
                "juan@email.com", "abcd", "321000002", "Carrera 5 #20-30"));

        clientes.put(3, new Cliente(3, "Lorena", "Torres",
                "lorena@email.com", "pass", "321000003", "Avenida 80 #30-40"));
                
    }

    public Cliente findById(Integer id) {
        return clientes.get(id);
    }

    public Optional<Cliente> findByCorreo(String correo) {
        return clientes.values().stream()
                .filter(c -> c.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    public boolean existeCorreo(String correo) {
        return clientes.values().stream()
                .anyMatch(c -> c.getCorreo().equalsIgnoreCase(correo));
    }

    public Cliente save(Cliente cliente) {
        cliente.setId(nextId++);
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    public Cliente update(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    public void delete(Integer id) {
        clientes.remove(id);
    }

    public Collection<Cliente> findAll() {
        return clientes.values();
    }
}