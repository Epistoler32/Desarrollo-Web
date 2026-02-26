package com.seaside.repository;
import com.seaside.model.Producto;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductoRepository {

    private Map<Integer, Producto> productos = new HashMap<>();

    public ProductoRepository() {
        productos.put(1,new Producto(1, "Ceviche SeaSide",
                "Pescado fresco marinado en limóncon cebolla morada, cilantro y el toque especial de la casa.",
                42000, "platos_fuertes"));
        productos.put(2, new Producto(2, "Arroz Marinero Especial",
                "Arroz preparado con camarones, calamares y especias que resaltan el sabor del mar.",
                58000.0, "platos_fuertes"));
        productos.put(3, new Producto(3, "Picada Marina SeaSide",
                "Selección de mariscos fritos, ideal para compartir.",
                76000.0, "platos_fuertes"));
        productos.put(4, new Producto(4, "Langosta Thermidor",
                "Langosta gratinada con salsa cremosa de vino blanco y queso.",
                85000.0, "platos_fuertes"));
        productos.put(5, new Producto(5, "Atun Sellado",
                "Lomo de atún sellado con ajonjolí y soya.",
                64000.0, "platos_fuertes"));
        productos.put(6, new Producto(6, "Pulpo a la Parrilla",
                "Pulpo tierno con aceite de oliva y pimentón ahumado.",
                69000.0, "platos_fuertes"));
        productos.put(7, new Producto(7, "Flan",
                "Postre tradicional de huevo y caramelo", 6.0, "postres_y_bebidas"));
        productos.put(8, new Producto(8, "Agua Fresca",
                "Bebida refrescante de frutas", 3.5, "postres_y_bebidas"));
    }

    public Producto findById(Integer id) {
        return productos.get(id);
    }

    public Collection<Producto> findAll() {
        return productos.values();
    }

}
