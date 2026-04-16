package com.seaside.controller;

import com.seaside.model.Adicionales;
import com.seaside.service.AdicionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;

@RestController
@RequestMapping("/api/adicionales")
public class AdicionalController {

    @Autowired
    private AdicionalService adicionalService;

    @GetMapping
    public ResponseEntity<Collection<Adicionales>> listAll() {
        return ResponseEntity.ok(adicionalService.findAll());
    }
}