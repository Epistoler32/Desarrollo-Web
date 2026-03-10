package com.seaside.repository;

import com.seaside.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

        Collection<Producto> findByCategoria_Nombre(String nombre);
        /*
         * } ProductoRepository {
         * 
         * // @Autowired
         * /*CategoriaRepository categoriaRepository;
         * private Map<Integer, Producto> productos = new HashMap<>();
         * 
         * @jakarta.annotation.PostConstruct
         * public void init() {
         * // --- PLATOS FUERTES ---
         * productos.put(1, new Producto(1, "Ceviche SeaSide",
         * "Pescado fresco marinado en limón con cebolla morada, cilantro y el toque especial de la casa."
         * ,
         * 42000.0, categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395516654-b7fca7a1-b01b-4731-86bb-11897cf4ac16.png",
         * 40,
         * true));
         * productos.put(2, new Producto(2, "Arroz Marinero Especial",
         * "Arroz preparado con camarones, calamares y especias que resaltan el sabor del mar."
         * ,
         * 58000.0, categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395477249-3fef9bb7-8834-48d7-98b7-a001da4e1d77.png",
         * 60,
         * true));
         * productos.put(3, new Producto(3, "Picada Marina SeaSide",
         * "Selección de mariscos fritos, ideal para compartir.", 76000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395549124-a58aacd7-3e0c-41d4-9fc5-7d8c4c6332db.png",
         * 40, true));
         * productos.put(4, new Producto(4, "Langosta Thermidor",
         * "Langosta gratinada con salsa cremosa de vino blanco y queso.", 85000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395574500-4fdbb3da-479c-4c26-87a0-52521d25e41a.png",
         * 50, true));
         * productos.put(5, new Producto(5, "Atún Sellado",
         * "Lomo de atún sellado con ajonjolí y soya.", 64000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395589341-866ec139-dd3e-4273-b5fd-b20215467068.png",
         * 40, true));
         * productos.put(6, new Producto(6, "Pulpo a la Parrilla",
         * "Pulpo tierno con aceite de oliva y pimentón ahumado.", 69000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395607903-ab2bc9b5-0377-419f-ad6f-a35c1175f07b.png",
         * 40, true));
         * productos.put(7, new Producto(7, "Salmón en Salsa de Eneldo",
         * "Filete de salmón a la plancha bañado en una crema suave de eneldo.",
         * 55000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395622880-5f4da79e-ed8a-4943-a5e4-d8a16a0faa24.png",
         * 45, true));
         * productos.put(8, new Producto(8, "Encocado de Pescado",
         * "Clásico costero con leche de coco, pimientos y pescado blanco.", 48000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395641103-e2ffaef2-7d6c-4565-b46e-740b1fc1ebec.png",
         * 35, true));
         * productos.put(9, new Producto(9, "Cazuela de Mariscos 'Old Harbor'",
         * "Combinación de frutos del mar en una base cremosa y profunda.", 62000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395654803-05d529d5-d81c-4d20-85f3-cb6957e0bcac.png",
         * 50, true));
         * productos.put(10, new Producto(10, "Filet Mignon del Puerto",
         * "Medallón de res envuelto en tocineta con salsa de pimienta negra.", 72000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395668152-153077e8-c995-4bd5-aa8b-3d17a0daea9a.png",
         * 45, true));
         * 
         * productos.put(11, new Producto(11, "Fettuccine Frutti di Mare",
         * "Pasta larga con calamares, camarones y mejillones en salsa pomodoro.",
         * 49000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395682396-2ed51622-ecdd-41ae-93fb-6da79a388683.png",
         * 30, true));
         * 
         * productos.put(12, new Producto(12, "Pargo Rojo Frito",
         * "Pargo entero crujiente servido con patacones y ensalada.", 65000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395699563-2de03292-113c-4045-aa38-006631732e5e.png",
         * 50, true));
         * 
         * productos.put(13, new Producto(13, "Camarones al Ajillo",
         * "Camarones salteados en mantequilla de ajo, perejil y vino blanco.", 52000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395714641-5483257a-9495-47f1-bc22-f265ea9e1d63.png",
         * 25, true));
         * 
         * productos.put(14, new Producto(14, "Risotto de Setas y Trufa",
         * "Para quienes prefieren una opción de tierra con sabor intenso.", 54000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395731833-82ff3284-d305-4eb8-b03c-80a6477b8518.png",
         * 40, true));
         * 
         * productos.put(15, new Producto(15, "Robalo en Costra de Almendras",
         * "Filete horneado con almendras fileteadas y mantequilla de limón.", 59000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395757630-4375172c-dca2-409f-929a-ce7032d3f563.png",
         * 45, true));
         * 
         * productos.put(16, new Producto(16, "Tacos de Pescado Baja Style",
         * "Pescado rebosado, col fresca y aderezo chipotle en tortilla de maíz.",
         * 38000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395797220-a0eab060-7b9b-452c-9bdd-35752c485b83.png",
         * 20, true));
         * 
         * productos.put(17, new Producto(17, "Curry de Langostinos",
         * "Langostinos en salsa curry amarillo con un toque picante suave.", 68000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395836776-f7d8fe6f-ff7e-498f-8641-0d74f9eb4716.png",
         * 40, true));
         * 
         * productos.put(18, new Producto(18, "Steak de Coliflor",
         * "Coliflor asada con especias, puré de garbanzo y aceite de hierbas.",
         * 32000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395883811-b0fd79c1-f057-4436-86f6-78477b238e3d.png",
         * 30, true));
         * 
         * productos.put(19, new Producto(19, "Paella SeaSide (Individual)",
         * "Arroz azafranado con el mix premium de la casa.", 74000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395924464-c7842027-9381-4adf-9bdb-84422493b1b5.png",
         * 55, true));
         * 
         * productos.put(20, new Producto(20, "Mojarra Premium",
         * "Mojarra roja frita de gran tamaño con arroz de coco.", 45000.0,
         * categoriaRepository.findById(1).getNombre(),
         * "https://image2url.com/r2/default/images/1772395947587-ec1fe964-eb3b-4560-b7c8-b5b5a8640c39.png",
         * 40, true));
         * 
         * // --- ADICIONALEs ---
         * productos.put(21, new Producto(21, "Flan",
         * "Postre tradicional de huevo y caramelo.", 12000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555602346-f0fce623-f5f2-4e48-8cdd-eca22c270d85.png",
         * 15, true));
         * 
         * productos.put(22, new Producto(22, "Cheesecake de Frutos Rojos",
         * "Base de galleta crocante con crema suave y coulis de mora/fresa.", 18000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555255134-bbc45394-6e44-41b7-9468-4ab5b957edb0.png",
         * 15, true));
         * 
         * productos.put(23, new Producto(23, "Mousse de Maracuyá",
         * "Crema aireada de fruta de la pasión con semillas crocantes.", 14000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555647637-81e870b9-e1cc-481e-a523-97ca7a0a6f36.png",
         * 10, true));
         * 
         * productos.put(24, new Producto(24, "Brownie con Helado de Vainilla",
         * "Servido caliente con salsa de chocolate amargo.", 16000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555195935-0f751858-1c31-4aca-8fd1-f57ac4645046.png",
         * 20, true));
         * 
         * productos.put(25, new Producto(25, "Pie de Limón",
         * "Merengue italiano sobre crema ácida de limón natural.", 15000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555167925-e69c1995-2442-4ac1-b9c1-417058abfca4.png",
         * 15, true));
         * 
         * productos.put(26, new Producto(26, "Volcán de Arequipe",
         * "Pastel fundente con corazón líquido de dulce de leche.", 19000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555131225-0bf9c274-a514-44eb-b0ec-2e476cc0e529.png",
         * 25, true));
         * 
         * productos.put(27, new Producto(27, "Agua Fresca",
         * "Bebida refrescante de frutas naturales (Sandía, Limonada, Melón).", 8000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555111888-fe47c4ba-0974-44af-ad0d-6490c14db4c4.png",
         * 5, true));
         * 
         * productos.put(28,
         * new Producto(28, "Limonada de Coco",
         * "Mezcla cremosa de limón y leche de coco fresca.",
         * 12000.0, categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555070111-668d8d82-e227-45b0-b0ca-0eda756db292.png",
         * 10, true));
         * 
         * productos.put(29,
         * new Producto(29, "Jugos Naturales",
         * "Mango, fresa, lulo o guanábana (en agua o leche).",
         * 9000.0, categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555045620-ed135899-159b-4018-a35f-c72113402558.png",
         * 10, true));
         * 
         * productos.put(30, new Producto(30, "Soda Saborizada",
         * "Mix de soda, sirope de la casa y fruta picada.",
         * 11000.0, categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772555008586-f2cd19fa-2689-4cf5-a679-3e0675393bb7.png",
         * 5, true));
         * 
         * productos.put(31,
         * new Producto(31, "Té Helado de la Casa",
         * "Infusión fría de té negro con durazno y menta.", 9500.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554958625-4340a022-ae7d-4e25-ac87-b41bc59dd81c.png",
         * 5, true));
         * 
         * productos.put(32,
         * new Producto(32, "Café Espresso / Americano",
         * "Café premium de origen para cerrar la comida.", 6000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554945245-052c7691-d361-4e59-8f51-09a271e5d08c.png",
         * 5, true));
         * 
         * productos.put(33, new Producto(33, "Patacones con Hogao",
         * "Plátano frito crujiente con salsa tradicional de tomate y cebolla.",
         * 14000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554930688-f90626ef-4c0e-4dda-a286-d7d4cc3e9428.png",
         * 20, true));
         * 
         * productos.put(34,
         * new Producto(34, "Canastas de Plátano con Camarón",
         * "Mini canastas rellenas de ceviche de camarón.", 24000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554914873-9102bcd9-4ae2-4719-b5d4-3b2e36a8b1ee.png",
         * 25, true));
         * productos.put(35,
         * new Producto(35, "Porción de Arroz de Coco",
         * "El acompañamiento dulce-salado infaltable.", 7000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554899744-86f570c4-824f-448b-89db-410236fccc68.png",
         * 10, true));
         * 
         * productos.put(36, new Producto(36, "Yucas Fritas",
         * "Bastones de yuca con suero costeño.", 12000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554884170-b5815f5f-5b69-4207-b093-a3da9b875099.png",
         * 15, true));
         * 
         * productos.put(37, new Producto(37, "Ensalada de la Casa",
         * "Mix de verdes, palmitos, aguacate y vinagreta cítrica.", 16000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554865453-a516247f-b228-47ef-99c0-3a542245d254.png",
         * 15, true));
         * 
         * productos.put(38, new Producto(38, "Aros de Calamar",
         * "Porción pequeña de calamares apanados con salsa tártara.", 22000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554798309-b6d507ff-a49e-4a50-99eb-98854263730e.png",
         * 20, true));
         * 
         * productos.put(39,
         * new Producto(39, "Papas Nativas al Horno",
         * "Con romero, sal marina y aceite de oliva.",
         * 13000.0, categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554783933-1c2458ed-4eba-440f-b71b-e8d4e683a267.png",
         * 20, true));
         * 
         * productos.put(40, new Producto(40, "Dip de Salmón Ahumado",
         * "Crema para untar servida con tostadas de pan artesanal.", 26000.0,
         * categoriaRepository.findById(2).getNombre(),
         * "https://image2url.com/r2/default/images/1772554761338-e4b74ec4-2890-4e40-9a43-c7c7c50d05fe.png",
         * 15, true));
         * 
         * }
         * 
         * public Producto findById(Integer id) {
         * return productos.get(id);
         * }
         * 
         * public Collection<Producto> findAll() {
         * return productos.values();
         * }
         * 
         * public Collection<Producto> findByCategory(String category) {
         * if (category == null) {
         * return List.of();
         * }
         * return productos.values().stream()
         * .filter(p -> {
         * String cat = p.getCategoria();
         * return cat != null && cat.equalsIgnoreCase(category);
         * })
         * .toList();
         * }
         * 
         * public void save(Producto producto) {
         * if (producto.getId() == null) {
         * int tam = productos.size();
         * int lastId = productos.get(tam).getId();
         * producto.setId(lastId + 1);
         * productos.put(producto.getId(), producto);
         * } else {
         * productos.put(producto.getId(), producto);
         * }
         * }
         * 
         * public void delete(Integer id) {
         * productos.remove(id);
         * }
         */
}
