# SeaSide

Aplicación web para el restaurante **SeaSide**, desarrollada con Spring Boot y Thymeleaf. Permite a los clientes explorar el menú, conocer el restaurante y hacer pedidos en línea.

---

## Tecnologías

- **Java 17+** con **Spring Boot**
- **Thymeleaf** como motor de plantillas
- **Spring MVC** (arquitectura Controller → Service → Repository)
- **HTML / CSS / JavaScript** vanilla para el frontend
- **Maven** como gestor de dependencias

---

## Estructura del proyecto

```
src/main/
├── java/com/seaside/
│   ├── controller/        # Controladores MVC
│   ├── model/             # Entidad Producto
│   ├── repository/        # Repositorio quemado en memoria (HashMap)
│   └── service/           # Interfaz + implementación de ProductoService
└── resources/
    ├── templates/         # Vistas Thymeleaf
    ├── static/
    │   ├── styles/        # CSS por página
    │   ├── js/            # JavaScript por página
    │   └── resources/IMGS # Imágenes del sitio
    └── application.properties
```

---

## Cómo correr el proyecto

### Prerrequisitos
- Java 17 o superior
- Maven

### Pasos

```bash
# Clona el repositorio
git clone https://github.com/Epistoler32/Desarrollo-Web.git
cd Desarrollo-Web

# Corre la aplicación
mvn spring-boot:run
```

Luego abre el navegador en `http://localhost:8080`.

---

## Vistas disponibles

| Ruta | Descripción |
|------|-------------|
| `/` | Landing page con hero, menú destacado y comentarios |
| `/menu` | Carta completa agrupada por categoría |
| `/products/listing` | Lista de productos para ser administrada por el administrador |
| `/products/{id}` | Información del producto individual |


---

## Categorías del menú

- **Platos Fuertes** — Ceviche, Arroz Marinero, Picada Marina, Langosta Thermidor, Atún Sellado, Pulpo a la Parrilla
- **Postres & Bebidas** — Flan, Agua Fresca

---

## Equipo
Nicolás Díaz Granados Cano
Juan Vera Garzón
Laura Aponte Sánchez
Sofia Guerra Jiménez