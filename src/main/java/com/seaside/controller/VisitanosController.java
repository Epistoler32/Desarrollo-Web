package com.seaside.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitanos")
public class VisitanosController {

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getSedes() {
        return ResponseEntity.ok(List.of(
                Map.of("nombre", "SeaSide Principal", "direccion", "Bogotá, Colombia")
        ));
    }
}