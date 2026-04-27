package com.seaside.controller;

import com.seaside.model.Domiciliario;
import com.seaside.service.DomiciliarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/domiciliarios")
public class DomiciliarioController {

    @Autowired
    private DomiciliarioService domiciliarioService;

    // GET /api/domiciliarios  o  ?disponibles=true
    @GetMapping
    public ResponseEntity<List<Domiciliario>> getAll(
            @RequestParam(name = "disponibles", required = false, defaultValue = "false") boolean disponibles) {
        List<Domiciliario> result = disponibles
                ? domiciliarioService.findDisponibles()
                : domiciliarioService.findAll();
        return ResponseEntity.ok(result);
    }

    // GET /api/domiciliarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return domiciliarioService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Domiciliario no encontrado: " + id)));
    }

    // POST /api/domiciliarios
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Domiciliario domiciliario) {
        try {
            Domiciliario saved = domiciliarioService.save(domiciliario);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/domiciliarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Domiciliario domiciliario) {
        return domiciliarioService.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    domiciliario.setId(id);
                    return ResponseEntity.ok(domiciliarioService.save(domiciliario));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Domiciliario no encontrado: " + id)));
    }

    // DELETE /api/domiciliarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return domiciliarioService.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    domiciliarioService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Domiciliario no encontrado: " + id)));
    }

    // PATCH /api/domiciliarios/{id}/disponibilidad
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<?> setDisponibilidad(
            @PathVariable Integer id,
            @RequestBody Map<String, Boolean> body) {
        Boolean disponible = body.get("disponible");
        if (disponible == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'disponible' es obligatorio"));
        }
        return domiciliarioService.findById(id)
                .<ResponseEntity<?>>map(d -> {
                    domiciliarioService.setDisponibilidad(id, disponible);
                    return ResponseEntity.ok(Map.of("message", "Disponibilidad actualizada"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Domiciliario no encontrado: " + id)));
    }

    // PATCH /api/domiciliarios/{id}/activo
    @PatchMapping("/{id}/activo")
    public ResponseEntity<?> setActivo(
            @PathVariable Integer id,
            @RequestBody Map<String, Boolean> body) {
        Boolean activo = body.get("activo");
        if (activo == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'activo' es obligatorio"));
        }
        return domiciliarioService.findById(id)
                .<ResponseEntity<?>>map(d -> {
                    domiciliarioService.setActivo(id, activo);
                    return ResponseEntity.ok(Map.of("message", "Estado activo actualizado"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Domiciliario no encontrado: " + id)));
    }
}