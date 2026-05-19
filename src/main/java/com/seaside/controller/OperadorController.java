package com.seaside.controller;

import com.seaside.dto.OperadorDTO;
import com.seaside.model.Operador;
import com.seaside.service.OperadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
Controlador REST para la gestión de operadores.

AGREGADO:
- GET endpoints retornan OperadorDTO (sin contraseña).
*/
@RestController
@RequestMapping("/api/operadores")
public class OperadorController {

    @Autowired
    private OperadorService operadorService;

    // GET /api/operadores
    @GetMapping
    public ResponseEntity<List<OperadorDTO>> getAll() {
        List<OperadorDTO> dtos = operadorService.findAll().stream()
                .map(OperadorDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /api/operadores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return operadorService.findById(id)
                .<ResponseEntity<?>>map(o -> ResponseEntity.ok(OperadorDTO.from(o)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Operador no encontrado con id: " + id)));
    }

    // POST /api/operadores
    @PostMapping
    public ResponseEntity<OperadorDTO> create(@RequestBody Operador operador) {
        operador.setId(null);
        Operador saved = operadorService.save(operador);
        return ResponseEntity.status(HttpStatus.CREATED).body(OperadorDTO.from(saved));
    }

    // PUT /api/operadores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Operador operador) {
        return operadorService.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    operador.setId(id);
                    return ResponseEntity.ok(OperadorDTO.from(operadorService.save(operador)));
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