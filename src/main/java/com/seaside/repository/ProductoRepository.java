package com.seaside.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seaside.model.Producto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class ProductoRepository {

    private final ObjectMapper objectMapper;

    public ProductoRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Producto> findByCategoria(String categoria) {
        try {
            ClassPathResource resource = new ClassPathResource("productos.json");
            // Read as Map<String, List<Producto>>
            java.util.Map<String, List<Producto>> all = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<java.util.Map<String, List<Producto>>>() {}
            );
            return all.getOrDefault(categoria, java.util.Collections.emptyList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading productos.json", e);
        }
    }
}