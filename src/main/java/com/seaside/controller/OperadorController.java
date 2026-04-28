package com.seaside.controller;

import com.seaside.model.Operador;
import com.seaside.service.OperadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de operadores (CRUD).
 * Toda la lógica de negocio se delega a OperadorService.
 */
@RestController
@RequestMapping("/api/operadores")
public class OperadorController {

    @Autowired
    private OperadorService operadorService;

    // GET /api/operadores — Lista todos los operadores
    @GetMapping
    public ResponseEntity<List<Operador>> getAll() {
        return ResponseEntity.ok(operadorService.findAll());
    }

    // GET /api/operadores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return operadorService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Operador no encontrado con id: " + id)));
    }

    // POST /api/operadores
    @PostMapping
    public ResponseEntity<Operador> create(@RequestBody Operador operador) {
        operador.setId(null); // JPA asigna el id
        Operador saved = operadorService.save(operador);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/operadores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Operador operador) {
        return operadorService.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    operador.setId(id);
                    return ResponseEntity.ok(operadorService.save(operador));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Operador no encontrado con id: " + id)));
    }

    // DELETE /api/operadores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return operadorService.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    operadorService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Operador no encontrado con id: " + id)));
    }
}