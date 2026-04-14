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

    // GET /api/domiciliarios
    // Acepta ?disponibles=true para filtrar solo los disponibles
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
                        .body(Map.of("error", "Domiciliario no encontrado con id: " + id)));
    }

    // PATCH /api/domiciliarios/{id}/disponibilidad
    // Body: { "disponible": true }
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
                        .body(Map.of("error", "Domiciliario no encontrado con id: " + id)));
    }
}