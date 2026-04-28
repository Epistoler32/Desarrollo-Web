package com.seaside.controller;

import com.seaside.model.Adicionales;
import com.seaside.service.AdicionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;


@RestController
@RequestMapping("/api/adicionales")
public class AdicionalController {

    @Autowired
    private AdicionalService adicionalService;

    // GET /api/adicionales

    @GetMapping
    public ResponseEntity<Collection<Adicionales>> listAll() {
        return ResponseEntity.ok(adicionalService.findAll());
    }

    // GET /api/adicionales/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Adicionales adicional = adicionalService.findById(id);
        if (adicional == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Adicional no encontrado con id: " + id));
        }
        return ResponseEntity.ok(adicional);
    }

    // POST /api/adicionales
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Adicionales adicional) {
        try {
            Adicionales saved = adicionalService.save(adicional);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al crear el adicional: " + e.getMessage()));
        }
    }

    // PUT /api/adicionales/{id}

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Adicionales adicional) {
        Adicionales existing = adicionalService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Adicional no encontrado con id: " + id));
        }
        adicional.setId(id);
        Adicionales updated = adicionalService.save(adicional);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/adicionales/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Adicionales existing = adicionalService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Adicional no encontrado con id: " + id));
        }
        adicionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}