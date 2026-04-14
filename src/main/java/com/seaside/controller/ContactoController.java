package com.seaside.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getContactInfo() {
        return ResponseEntity.ok(Map.of(
                "email", "contacto@seaside.com",
                "telefono", "+57 300 000 0000",
                "direccion", "Bogotá, Colombia"
        ));
    }
}