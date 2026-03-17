package com.seaside;

import com.seaside.model.Categoria;
import com.seaside.model.Cliente;
import com.seaside.model.Producto;
import com.seaside.repository.CategoriaRepository;
import com.seaside.repository.ProductoRepository;
import com.seaside.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {

        @Autowired
        private CategoriaRepository categoriaRepository;

        @Autowired
        private ProductoRepository productoRepository;

        @Autowired
        private ClienteRepository clienteRepository;

        @Override
        public void run(String... args) throws Exception {

                Categoria platosFuertes;
                Categoria acompanamientos;
                Categoria bebidas;
                Categoria postres;
                Categoria entradas;

                if (categoriaRepository.count() == 0) {

                        platosFuertes = categoriaRepository.save(new Categoria("Platos Fuertes"));
                        acompanamientos = categoriaRepository.save(new Categoria("Acompañamientos"));
                        bebidas = categoriaRepository.save(new Categoria("Bebidas"));
                        postres = categoriaRepository.save(new Categoria("Postres"));
                        entradas = categoriaRepository.save(new Categoria("Entradas"));

                } else {

                        platosFuertes = categoriaRepository.findByNombre("Platos Fuertes");
                        acompanamientos = categoriaRepository.findByNombre("Acompañamientos");
                        bebidas = categoriaRepository.findByNombre("Bebidas");
                        postres = categoriaRepository.findByNombre("Postres");
                        entradas = categoriaRepository.findByNombre("Entradas");

                }

                if (productoRepository.count() == 0) {

                        // ======================================================
                        // PLATOS FUERTES
                        // ======================================================
                        productoRepository.save(new Producto(null, "Ceviche SeaSide",
                                        "Pescado fresco marinado en limón con cebolla morada, cilantro y el toque especial de la casa.",
                                        42000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395516654-b7fca7a1-b01b-4731-86bb-11897cf4ac16.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Arroz Marinero Especial",
                                        "Arroz preparado con camarones, calamares y especias que resaltan el sabor del mar.",
                                        58000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395477249-3fef9bb7-8834-48d7-98b7-a001da4e1d77.png",
                                        60, true));
                        productoRepository.save(new Producto(null, "Langosta Thermidor",
                                        "Langosta gratinada con salsa cremosa de vino blanco y queso.", 85000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395574500-4fdbb3da-479c-4c26-87a0-52521d25e41a.png",
                                        50, true));
                        productoRepository.save(new Producto(null, "Atún Sellado",
                                        "Lomo de atún sellado con ajonjolí y soya.", 64000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395589341-866ec139-dd3e-4273-b5fd-b20215467068.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Pulpo a la Parrilla",
                                        "Pulpo tierno con aceite de oliva y pimentón ahumado.", 69000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395607903-ab2bc9b5-0377-419f-ad6f-a35c1175f07b.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Salmón en Salsa de Eneldo",
                                        "Filete de salmón a la plancha bañado en una crema suave de eneldo.", 55000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395622880-5f4da79e-ed8a-4943-a5e4-d8a16a0faa24.png",
                                        45, true));
                        productoRepository.save(new Producto(null, "Encocado de Pescado",
                                        "Clásico costero con leche de coco, pimientos y pescado blanco.", 48000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395641103-e2ffaef2-7d6c-4565-b46e-740b1fc1ebec.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Cazuela de Mariscos 'Old Harbor'",
                                        "Combinación de frutos del mar en una base cremosa y profunda.", 62000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395654803-05d529d5-d81c-4d20-85f3-cb6957e0bcac.png",
                                        50, true));
                        productoRepository.save(new Producto(null, "Filet Mignon del Puerto",
                                        "Medallón de res envuelto en tocineta con salsa de pimienta negra.", 72000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395668152-153077e8-c995-4bd5-aa8b-3d17a0daea9a.png",
                                        45, true));
                        productoRepository.save(new Producto(null, "Fettuccine Frutti di Mare",
                                        "Pasta larga con calamares, camarones y mejillones en salsa pomodoro.", 49000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395682396-2ed51622-ecdd-41ae-93fb-6da79a388683.png",
                                        30, true));
                        productoRepository.save(new Producto(null, "Pargo Rojo Frito",
                                        "Pargo entero crujiente servido con patacones y ensalada.", 65000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395699563-2de03292-113c-4045-aa38-006631732e5e.png",
                                        50, true));
                        productoRepository.save(new Producto(null, "Picada Marina SeaSide",
                                        "Selección de mariscos fritos, ideal para compartir.", 76000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395549124-a58aacd7-3e0c-41d4-9fc5-7d8c4c6332db.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Risotto de Setas y Trufa",
                                        "Para quienes prefieren una opción de tierra con sabor intenso.", 54000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395731833-82ff3284-d305-4eb8-b03c-80a6477b8518.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Robalo en Costra de Almendras",
                                        "Filete horneado con almendras fileteadas y mantequilla de limón.", 59000.0,
                                        platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395757630-4375172c-dca2-409f-929a-ce7032d3f563.png",
                                        45, true));
                        productoRepository.save(new Producto(null, "Paella SeaSide (Individual)",
                                        "Arroz azafranado con el mix premium de la casa.", 74000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395924464-c7842027-9381-4adf-9bdb-84422493b1b5.png",
                                        55, true));
                        productoRepository.save(new Producto(null, "Mojarra Premium",
                                        "Mojarra roja frita de gran tamaño con arroz de coco.", 45000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1772395947587-ec1fe964-eb3b-4560-b7c8-b5b5a8640c39.png",
                                        40, true));


                        productoRepository.save(new Producto(null, "Tiradito de Atún",
                                        "Láminas finas de atún fresco con leche de tigre amarilla y jalapeño.",
                                        47000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773714772384-831dd70c-f469-40f3-b009-b60d778aedef.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Corvina a la Criolla",
                                        "Corvina en salsa de tomate, cebolla, ají dulce y hierbas del pacífico.",
                                        50000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773714838450-7e406df0-5fb8-4223-92e4-055339382c94.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Almejas al Vapor con Vino Blanco",
                                        "Almejas frescas abiertas al vapor con ajo, mantequilla y perejil.",
                                        53000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773714876272-92a7d633-de67-4b9f-998c-b87c4143ae79.png",
                                        30, true));
                        productoRepository.save(new Producto(null, "Cangrejo Gratinado",
                                        "Caparazón de cangrejo relleno con gratín de queso gruyère y hierbas finas.",
                                        78000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773714923755-5e02e11f-bec1-41b5-9ce0-74205d70803d.png",
                                        25, true));
                        productoRepository.save(new Producto(null, "Langostinos al Curry Verde",
                                        "Langostinos en salsa thai de curry verde con leche de coco y albahaca.",
                                        66000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773714970483-b30cdbb7-b583-401f-8fcc-724891c494f6.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Trucha con Alcaparras",
                                        "Trucha a la plancha con salsa de mantequilla dorada, alcaparras y limón.",
                                        51000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715026066-602f8f3b-d66a-4ddf-b251-85ff8cbcba71.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Bacalao a la Vizcaína",
                                        "Bacalao desalado en salsa de pimientos rojos asados y aceitunas negras.",
                                        56000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715064538-9265713d-31ec-4681-8c2d-693bdaa4f84f.png",
                                        30, true));
                        productoRepository.save(new Producto(null, "Pasta Negra con Mariscos",
                                        "Pasta en tinta de calamar con frutos del mar frescos y aceite de albahaca.",
                                        60000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715094837-38291554-be6d-42d2-beab-54d06d0ae37b.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Mahi-Mahi Tropical",
                                        "Dorado a la plancha con salsa de mango y piña asada, servido con arroz.",
                                        53000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715139954-6b83200d-e061-4eb6-9da3-7f6f6472dd70.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Calamares Rellenos Mediterráneos",
                                        "Calamares rellenos de arroz, tomate seco y hierbas, horneados al horno.",
                                        52000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715175583-e152ea66-e13b-4160-850f-3145c8ee535b.png",
                                        30, true));
                        productoRepository.save(new Producto(null, "Corvina en Papillote",
                                        "Corvina horneada en papel con limón, vegetales frescos y hierbas aromáticas.",
                                        49000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715213589-e7a9c1d7-ea3d-4b6c-9ec7-81bca68dd8ac.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Wok de Mariscos Asiático",
                                        "Camarones, vieiras y vegetales salteados en salsa de soya, jengibre y sésamo.",
                                        61000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715268289-d29b9c11-44d9-4bac-80a6-97675afbd58c.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Bouillabaisse SeaSide",
                                        "Sopa provenzal de mariscos con rouille de azafrán y pan artesanal tostado.",
                                        67000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715330854-0647e81f-d849-4349-9ee7-f1e56fa20f9f.png",
                                        30, true));
                        productoRepository.save(new Producto(null, "Medallones de Vieira",
                                        "Vieiras selladas sobre puré de coliflor con mantequilla de trufa negra.",
                                        73000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715359273-7dd1f26e-080b-4155-998a-aa38c696bd9d.png",
                                        25, true));
                        productoRepository.save(new Producto(null, "Chupe de Mariscos",
                                        "Guiso espeso tradicional con mariscos variados, papa criolla y crema de leche.",
                                        54000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715388397-657a286b-ada6-487e-8522-307555f114a4.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Lubina a la Sal Marina",
                                        "Lubina entera horneada en costra de sal con tomillo y romero fresco.",
                                        76000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715420215-36e5ba1f-4a11-4608-930d-4d94d6c9035d.png",
                                        30, true));
                        productoRepository.save(new Producto(null, "Tagliatelle con Langosta",
                                        "Pasta artesanal con trozos de langosta en bisque cremoso y cebollín.",
                                        80000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715456478-89d33df2-5235-4103-b407-9e16860aae06.png",
                                        25, true));
                        productoRepository.save(new Producto(null, "Ceviche Mixto del Pacífico",
                                        "Camarón, pulpo y pescado en leche de tigre verde con aguacate y maíz.",
                                        46000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715484240-4cf6b058-21a5-4199-a13c-e3a25d1b6fc1.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Pescado a la Veracruzana",
                                        "Filete de pescado en salsa de olivas, alcaparras y jitomate estilo mexicano.",
                                        50000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715547367-cc352f47-4efa-40c2-a2f4-debf61cff890.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Gambas a la Plancha",
                                        "Camarones tigre a la plancha con mantequilla de ajo tostado y limón fresco.",
                                        63000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715565692-2e6dfe3e-dd21-4f02-a455-111f78efa13e.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Sudado de Pescado Costero",
                                        "Cocción lenta en caldo de tomate, coco y especias costeñas con vegetales.",
                                        47000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715584638-3c111359-132a-435c-b4d3-8776debe0d1c.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Churrasco de Pez Espada",
                                        "Filete grueso de pez espada a la parrilla con chimichurri de hierbas del mar.",
                                        70000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715621531-7aab5370-0a72-459f-9dbb-864fc7d57114.png",
                                        35, true));
                        productoRepository.save(new Producto(null, "Tilapia en Salsa de Maracuyá",
                                        "Filete de tilapia horneado con reducción de maracuyá, miel y mostaza Dijon.",
                                        44000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715734522-b8bd36d9-5278-462f-9572-1fb8d24d5572.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Bogavante a la Mantequilla",
                                        "Medio bogavante a la plancha con mantequilla clarificada y limón Meyer.",
                                        95000.0, platosFuertes,
                                        "https://image2url.com/r2/default/images/1773715756354-fb72998c-e166-463e-a092-3f3bca7b7295.png",
                                        20, true));

                        // ======================================================
                        // ENTRADAS
                        // ======================================================

                        productoRepository.save(new Producto(null, "Brochetas de Mariscos a la Parrilla",
                                        "Pinchos de camarón, calamar y pescado marinados en cítricos y especias.",
                                        57000.0, entradas,
                                        "https://image2url.com/r2/default/images/1773715803146-d92c1d99-b9bc-41ed-a86f-1fd897cf8a95.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Camarones al Ajillo",
                                        "Camarones salteados en mantequilla de ajo, perejil y vino blanco.", 52000.0,
                                        entradas,
                                        "https://image2url.com/r2/default/images/1772395714641-5483257a-9495-47f1-bc22-f265ea9e1d63.png",
                                        25, true));
                        productoRepository.save(new Producto(null, "Tacos de Pescado Baja Style",
                                        "Pescado rebosado, col fresca y aderezo chipotle en tortilla de maíz.", 38000.0,
                                        entradas,
                                        "https://image2url.com/r2/default/images/1772395797220-a0eab060-7b9b-452c-9bdd-35752c485b83.png",
                                        20, true));
                        productoRepository.save(new Producto(null, "Curry de Langostinos",
                                        "Langostinos en salsa curry amarillo con un toque picante suave.", 68000.0,
                                        entradas,
                                        "https://image2url.com/r2/default/images/1772395836776-f7d8fe6f-ff7e-498f-8641-0d74f9eb4716.png",
                                        40, true));
                        productoRepository.save(new Producto(null, "Canastas de Plátano con Camarón",
                                        "Mini canastas rellenas de ceviche de camarón.", 24000.0, entradas,
                                        "https://image2url.com/r2/default/images/1772554914873-9102bcd9-4ae2-4719-b5d4-3b2e36a8b1ee.png",
                                        25, true));
                        productoRepository.save(new Producto(null, "Aros de Calamar",
                                        "Porción pequeña de calamares apanados con salsa tártara.", 22000.0, entradas,
                                        "https://image2url.com/r2/default/images/1772554798309-b6d507ff-a49e-4a50-99eb-98854263730e.png",
                                        20, true));
                        productoRepository.save(new Producto(null, "Dip de Salmón Ahumado",
                                        "Crema para untar servida con tostadas de pan artesanal.", 26000.0, entradas,
                                        "https://image2url.com/r2/default/images/1772554761338-e4b74ec4-2890-4e40-9a43-c7c7c50d05fe.png",
                                        15, true));

                        // ======================================================
                        // ACOMPAÑAMIENTOS
                        // ======================================================

                        productoRepository.save(new Producto(null, "Steak de Coliflor",
                                        "Coliflor asada con especias, puré de garbanzo y aceite de hierbas.", 32000.0,
                                        acompanamientos,
                                        "https://image2url.com/r2/default/images/1772395883811-b0fd79c1-f057-4436-86f6-78477b238e3d.png",
                                        30, true));
                        productoRepository.save(new Producto(null, "Patacones con Hogao",
                                        "Plátano frito crujiente con salsa tradicional de tomate y cebolla.", 14000.0,
                                        acompanamientos,
                                        "https://image2url.com/r2/default/images/1772554930688-f90626ef-4c0e-4dda-a286-d7d4cc3e9428.png",
                                        20, true));
                        productoRepository.save(new Producto(null, "Porción de Arroz de Coco",
                                        "El acompañamiento dulce-salado infaltable.", 7000.0, acompanamientos,
                                        "https://image2url.com/r2/default/images/1772554899744-86f570c4-824f-448b-89db-410236fccc68.png",
                                        10, true));
                        productoRepository.save(new Producto(null, "Yucas Fritas",
                                        "Bastones de yuca con suero costeño.", 12000.0, acompanamientos,
                                        "https://image2url.com/r2/default/images/1772554884170-b5815f5f-5b69-4207-b093-a3da9b875099.png",
                                        15, true));
                        productoRepository.save(new Producto(null, "Ensalada de la Casa",
                                        "Mix de verdes, palmitos, aguacate y vinagreta cítrica.", 16000.0,
                                        acompanamientos,
                                        "https://image2url.com/r2/default/images/1772554865453-a516247f-b228-47ef-99c0-3a542245d254.png",
                                        15, true));
                        productoRepository.save(new Producto(null, "Papas Nativas al Horno",
                                        "Con romero, sal marina y aceite de oliva.", 13000.0, acompanamientos,
                                        "https://image2url.com/r2/default/images/1772554783933-1c2458ed-4eba-440f-b71b-e8d4e683a267.png",
                                        20, true));

                        // ======================================================
                        // POSTRES
                        // ======================================================

                        productoRepository.save(new Producto(null, "Flan",
                                        "Postre tradicional de huevo y caramelo.", 12000.0, postres,
                                        "https://image2url.com/r2/default/images/1772555602346-f0fce623-f5f2-4e48-8cdd-eca22c270d85.png",
                                        15, true));
                        productoRepository.save(new Producto(null, "Cheesecake de Frutos Rojos",
                                        "Base de galleta crocante con crema suave y coulis de mora/fresa.", 18000.0,
                                        postres,
                                        "https://image2url.com/r2/default/images/1772555255134-bbc45394-6e44-41b7-9468-4ab5b957edb0.png",
                                        15, true));
                        productoRepository.save(new Producto(null, "Mousse de Maracuyá",
                                        "Crema aireada de fruta de la pasión con semillas crocantes.", 14000.0, postres,
                                        "https://image2url.com/r2/default/images/1772555647637-81e870b9-e1cc-481e-a523-97ca7a0a6f36.png",
                                        10, true));
                        productoRepository.save(new Producto(null, "Brownie con Helado de Vainilla",
                                        "Servido caliente con salsa de chocolate amargo.", 16000.0, postres,
                                        "https://image2url.com/r2/default/images/1772555195935-0f751858-1c31-4aca-8fd1-f57ac4645046.png",
                                        20, true));
                        productoRepository.save(new Producto(null, "Pie de Limón",
                                        "Merengue italiano sobre crema ácida de limón natural.", 15000.0, postres,
                                        "https://image2url.com/r2/default/images/1772555167925-e69c1995-2442-4ac1-b9c1-417058abfca4.png",
                                        15, true));
                        productoRepository.save(new Producto(null, "Volcán de Arequipe",
                                        "Pastel fundente con corazón líquido de dulce de leche.", 19000.0, postres,
                                        "https://image2url.com/r2/default/images/1772555131225-0bf9c274-a514-44eb-b0ec-2e476cc0e529.png",
                                        25, true));

                        // ======================================================
                        // BEBIDAS
                        // ======================================================

                        productoRepository.save(new Producto(null, "Agua Fresca",
                                        "Bebida refrescante de frutas naturales.", 8000.0, bebidas,
                                        "https://image2url.com/r2/default/images/1772555111888-fe47c4ba-0974-44af-ad0d-6490c14db4c4.png",
                                        5, true));
                        productoRepository.save(new Producto(null, "Limonada de Coco",
                                        "Mezcla cremosa de limón y leche de coco fresca.", 12000.0, bebidas,
                                        "https://image2url.com/r2/default/images/1772555070111-668d8d82-e227-45b0-b0ca-0eda756db292.png",
                                        10, true));
                        productoRepository.save(new Producto(null, "Jugos Naturales",
                                        "Mango, fresa, lulo o guanábana (en agua o leche).", 9000.0, bebidas,
                                        "https://image2url.com/r2/default/images/1772555045620-ed135899-159b-4018-a35f-c72113402558.png",
                                        10, true));
                        productoRepository.save(new Producto(null, "Soda Saborizada",
                                        "Mix de soda, sirope de la casa y fruta picada.", 11000.0, bebidas,
                                        "https://image2url.com/r2/default/images/1772555008586-f2cd19fa-2689-4cf5-a679-3e0675393bb7.png",
                                        5, true));
                        productoRepository.save(new Producto(null, "Té Helado de la Casa",
                                        "Infusión fría de té negro con durazno y menta.", 9500.0, bebidas,
                                        "https://image2url.com/r2/default/images/1772554958625-4340a022-ae7d-4e25-ac87-b41bc59dd81c.png",
                                        5, true));
                        productoRepository.save(new Producto(null, "Café Espresso / Americano",
                                        "Café premium de origen para cerrar la comida.", 6000.0, bebidas,
                                        "https://image2url.com/r2/default/images/1772554945245-052c7691-d361-4e59-8f51-09a271e5d08c.png",
                                        5, true));

                        // ======================================================
                        // CLIENTES
                        // ======================================================

                        clienteRepository.save(new Cliente("Laura", "Gomez", "laura.gomez@email.com", "1234",
                                        "3001234567", "Cra 15 #93-20"));
                        clienteRepository.save(new Cliente("Carlos", "Ramirez", "carlos.ramirez@email.com", "1234",
                                        "3002345678", "Calle 80 #45-12"));
                        clienteRepository.save(new Cliente("Ana", "Martinez", "ana.martinez@email.com", "1234",
                                        "3003456789", "Cra 7 #45-33"));
                        clienteRepository.save(new Cliente("Juan", "Torres", "juan.torres@email.com", "1234",
                                        "3004567890", "Calle 26 #68-45"));
                        clienteRepository.save(new Cliente("Maria", "Lopez", "maria.lopez@email.com", "1234",
                                        "3005678901", "Cra 50 #12-80"));
                        clienteRepository.save(new Cliente("Andres", "Castro", "andres.castro@email.com", "1234",
                                        "3006789012", "Calle 100 #19-30"));
                        clienteRepository.save(new Cliente("Sofia", "Herrera", "sofia.herrera@email.com", "1234",
                                        "3007890123", "Cra 11 #72-15"));
                        clienteRepository.save(new Cliente("Diego", "Vargas", "diego.vargas@email.com", "1234",
                                        "3008901234", "Calle 53 #27-60"));
                        clienteRepository.save(new Cliente("Valentina", "Rojas", "valentina.rojas@email.com", "1234",
                                        "3009012345", "Cra 9 #134-22"));
                        clienteRepository.save(new Cliente("Mateo", "Castano", "mateo.castano@email.com", "1234",
                                        "3000123456", "Calle 170 #15-44"));
                }
        }
}