package com.seaside.controller;

import com.seaside.model.Cliente;
import com.seaside.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<Collection<Cliente>> listClients() {
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClientById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody Cliente cliente) {
        if (clienteService.existeCorreo(cliente.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe una cuenta con ese correo."));
        }
        Cliente guardado = clienteService.registrarNuevo(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateClient(@PathVariable("id") Integer id, @RequestBody Cliente cliente) {
        cliente.setId(id);
        Cliente actualizado = clienteService.actualizar(cliente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Integer id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}