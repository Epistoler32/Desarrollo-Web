# SeaSide — Restaurante de Mar

Aplicación web full-stack para el restaurante **SeaSide**, desarrollada con Spring Boot y Thymeleaf. Permite a los clientes explorar el menú completo, registrarse, gestionar su perfil y ver el detalle de cada plato. Incluye un panel de administración para gestionar el catálogo de productos.

---

## Funcionalidades

**Clientes**
- Registro e inicio de sesión con sesión HTTP
- Ver y editar perfil personal
- Eliminar cuenta

**Menú**
- Carta completa agrupada por categoría (Platos Fuertes, Entradas, Acompañamientos, Postres, Bebidas)
- Detalle individual de cada producto con imagen, precio, tiempo de preparación e indicador de alérgenos

**Administración**
- Listar, crear, actualizar y eliminar productos desde `/products/listing`

**Otras páginas**
- Landing page con hero, menú destacado y sección de comentarios
- Página de contacto con formulario, información del restaurante y mapa embebido

---

## Tecnologías

| Capa | Tecnología |
|------|-----------|
| Backend | Java 17+, Spring Boot, Spring MVC, Spring Data JPA |
| Base de datos | H2 (archivo persistente `mydatabase`) |
| Plantillas | Thymeleaf |
| Frontend | HTML5, CSS3, JavaScript vanilla, Bootstrap 5 (login) |
| Build | Maven |
| Utilidades | Lombok |

---

## Estructura del proyecto

```
src/main/
├── java/com/seaside/
│   ├── config/            # Configuración H2 Console
│   ├── controller/        # AuthController, ClientController,
│   │                      # ProductoController, MenuController,
│   │                      # LandingController, ContactoController
│   ├── errors/            # Manejo global de excepciones
│   ├── model/             # Entidades JPA: Categoria, Producto, Cliente
│   ├── repository/        # Repositorios Spring Data JPA
│   ├── service/           # Interfaces + implementaciones de servicios
│   └── DataLoader.java    # Datos iniciales (categorías, productos, clientes)
└── resources/
    ├── templates/         # Vistas Thymeleaf (.html)
    ├── static/
    │   ├── styles/        # CSS por página
    │   ├── js/            # JavaScript por página
    │   └── resources/IMGS/# Imágenes del sitio
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

Abre el navegador en `http://localhost:8080`.

> **Nota:** La base de datos se recrea automáticamente en cada arranque (`ddl-auto=create-drop`) y se puebla con datos de prueba via `DataLoader`. Los datos de prueba incluyen 10 clientes con correo `*@email.com` y contraseña `1234`.

### Consola H2

Accede a `http://localhost:8080/h2` con:
- **JDBC URL:** `jdbc:h2:file:./mydatabase`
- **Usuario:** `sa`
- **Contraseña:** *(vacía)*

---

## Rutas disponibles

| Ruta | Descripción |
|------|-------------|
| `GET /` | Landing page |
| `GET /menu` | Carta completa agrupada por categoría |
| `GET /products/{id}` | Detalle de un producto |
| `GET /products/listing` | Panel admin - listado de productos |
| `GET /products/create` | Panel admin - formulario nuevo producto |
| `GET /products/update/{id}` | Panel admin - editar producto |
| `GET /products/delete/{id}` | Panel admin - eliminar producto |
| `GET /contacto` | Página de contacto |
| `GET /login` | Formulario de inicio de sesión |
| `POST /login` | Procesar login |
| `GET /signup` | Formulario de registro |
| `POST /signup` | Procesar registro |
| `GET /logout` | Cerrar sesión |
| `GET /clients/profile` | Perfil del cliente logueado |
| `GET /clients/update/{id}` | Editar perfil |
| `POST /clients/update` | Guardar cambios de perfil |
| `GET /clients/delete/{id}` | Eliminar cuenta |

---

## Categorías del menú

| Categoría | Ejemplos |
|-----------|---------|
| Platos Fuertes | Ceviche SeaSide, Langosta Thermidor, Paella SeaSide, Bogavante a la Mantequilla... |
| Entradas | Camarones al Ajillo, Aros de Calamar, Tacos de Pescado Baja Style... |
| Acompañamientos | Patacones con Hogao, Arroz de Coco, Ensalada de la Casa... |
| Postres | Volcán de Arequipe, Cheesecake de Frutos Rojos, Pie de Limón... |
| Bebidas | Limonada de Coco, Jugos Naturales, Té Helado de la Casa... |

---

## Equipo

- Nicolás Díaz Granados Cano  
- Juan Esteban Vera Garzón  
- Laura Sofía Aponte Sánchez  
- Sofia Guerra Jiménez
