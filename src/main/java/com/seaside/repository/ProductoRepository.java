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
                productos.put(1, new Producto(1, "Ceviche SeaSide",
                                "Pescado fresco marinado en limóncon cebolla morada, cilantro y el toque especial de la casa.",
                                42000, "platos_fuertes",
                                "https://image2url.com/r2/default/images/1772078203955-e709d8d3-de0c-48f2-abc7-74a2b6d9c499.png",
                                40, true));
                productos.put(2, new Producto(2, "Arroz Marinero Especial",
                                "Arroz preparado con camarones, calamares y especias que resaltan el sabor del mar.",
                                58000.0, "platos_fuertes",
                                "https://image2url.com/r2/default/images/1772078574837-34051a06-9bee-4da7-b78a-ce5016017424.png",
                                60, true));
                productos.put(3, new Producto(3, "Picada Marina SeaSide",
                                "Selección de mariscos fritos, ideal para compartir.",
                                76000.0, "platos_fuertes",
                                "https://image2url.com/r2/default/images/1772078606263-bcccee24-abf3-40df-a770-2ad1a31a2cc2.png",
                                40, true));
                productos.put(4, new Producto(4, "Langosta Thermidor",
                                "Langosta gratinada con salsa cremosa de vino blanco y queso.",
                                85000.0, "platos_fuertes",
                                "https://image2url.com/r2/default/images/1772078631567-234de52d-696e-4eb0-8df2-a66be45f06c9.png",
                                50, true));
                productos.put(5, new Producto(5, "Atun Sellado",
                                "Lomo de atún sellado con ajonjolí y soya.",
                                64000.0, "platos_fuertes",
                                "https://image2url.com/r2/default/images/1772078641606-d7825080-0921-42fa-9859-93c6f7ed80d5.png",
                                40, true));
                productos.put(6, new Producto(6, "Pulpo a la Parrilla",
                                "Pulpo tierno con aceite de oliva y pimentón ahumado.",
                                69000.0, "platos_fuertes",
                                "https://image2url.com/r2/default/images/1772078666703-9692bbed-35d8-4861-9eee-ebd299b87583.png",
                                40, true));
                productos.put(7, new Producto(7, "Flan",
                                "Postre tradicional de huevo y caramelo", 6.0, "postres_y_bebidas",
                                "https://image2url.com/r2/default/images/1772078693510-053e9d81-7512-4a72-bcf6-bc59db6d7684.png",
                                20, true));
                productos.put(8, new Producto(8, "Agua Fresca",
                                "Bebida refrescante de frutas", 3.5, "postres_y_bebidas", "https://image2url.com/r2/default/images/1772078727798-cda82f6c-b260-47ad-a116-dfd356560169.png", 10, false));
        }

        public Producto findById(Integer id) {
                return productos.get(id);
        }

        public Collection<Producto> findAll() {
                return productos.values();
        }

}
