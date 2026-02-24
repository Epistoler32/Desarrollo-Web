package com.seaside.repository;

import com.seaside.model.Producto;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple map-backed implementation of {@link ProductoRepository}.  Acts as a
 * mock database for development/testing.
 */
@Repository
public class MapProductoRepository implements ProductoRepository {

    private final Map<String, List<Producto>> store = new HashMap<>();

    public MapProductoRepository() {
        // initialize with values that were previously stored in productos.json
        List<Producto> fuertes = new java.util.ArrayList<>();
        fuertes.add(new Producto("Ceviche SeaSide",
                "Pescado fresco marinado en limóncon cebolla morada, cilantro y el toque especial de la casa.",
                42000, "platos_fuertes"));
        fuertes.add(new Producto("Arroz Marinero Especial",
                "Arroz preparado con camarones, calamares y especias que resaltan el sabor del mar.",
                58000.0, "platos_fuertes"));
        fuertes.add(new Producto("Picada Marina SeaSide",
                "Selección de mariscos fritos, ideal para compartir.",
                76000.0, "platos_fuertes"));
        fuertes.add(new Producto("Langosta Thermidor",
                "Langosta gratinada con salsa cremosa de vino blanco y queso.",
                85000.0, "platos_fuertes"));
        fuertes.add(new Producto("Atun Sellado",
                "Lomo de atún sellado con ajonjolí y soya.",
                64000.0, "platos_fuertes"));
        fuertes.add(new Producto("Pulpo a la Parrilla",
                "Pulpo tierno con aceite de oliva y pimentón ahumado.",
                69000.0, "platos_fuertes"));

        List<Producto> postres = new java.util.ArrayList<>();
        postres.add(new Producto("Flan",
                "Postre tradicional de huevo y caramelo", 6.0, "postres_y_bebidas"));
        postres.add(new Producto("Agua Fresca",
                "Bebida refrescante de frutas", 3.5, "postres_y_bebidas"));

        store.put("platos_fuertes", fuertes);
        store.put("postres_y_bebidas", postres);
    }

    @Override
    public List<Producto> findByCategoria(String categoria) {
        return store.getOrDefault(categoria, Collections.emptyList());
    }

    @Override
    public List<Producto> findAll() {
        return store.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public List<Producto> findByID(String nombre) {
        // simple filter by exact match of name
        return findAll().stream()
                .filter(p -> p.getNombre().equals(nombre))
                .toList();
    }
    
    public void put(String categoria, List<Producto> productos) {
        store.put(categoria, productos);
    }
}
