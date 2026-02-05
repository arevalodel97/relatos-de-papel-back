# 📚 ms-books-catalogue

Microservicio REST para la gestión del catálogo de libros - "Relatos de Papel"

## 🚀 Inicio Rápido

### Pre-requisitos
- Java 25
- Maven 3.6+
- MySQL 8+

### Ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

El microservicio estará disponible en `http://localhost:8081`

---

## 📖 Documentación API (Swagger)

```
http://localhost:8081/swagger-ui.html
```

---

## 🌐 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/books` | Listar libros visibles |
| GET | `/books/{id}` | Obtener libro por ID |
| POST | `/books` | Crear libro |
| PUT | `/books/{id}` | Actualizar completo |
| PATCH | `/books/{id}` | Actualizar parcial |
| DELETE | `/books/{id}` | Eliminar libro |
| GET | `/books/search?params` | Búsqueda dinámica |

### Búsqueda con filtros

```
/books/search?title=&author=&publishedDate=&category=&isbn=&rating=&visible=
```

**Ejemplos:**
```bash
# Buscar por autor
curl "http://localhost:8081/books/search?author=garcía"

# Buscar fantasía con rating 5
curl "http://localhost:8081/books/search?category=fantasía&rating=5"
```

---

## 🔧 Configuración

### Variables de Entorno

Copia `.env.example` a `.env` y ajusta los valores:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=books_catalogue_db
DB_USERNAME=root
DB_PASSWORD=12345
SERVER_PORT=8081
```

### Cargar variables de entorno

```bash
# Linux/Mac
export $(cat .env | xargs)
./mvnw spring-boot:run

# Windows (PowerShell)
Get-Content .env | ForEach-Object { $var = $_.Split('='); [Environment]::SetEnvironmentVariable($var[0], $var[1]) }
./mvnw spring-boot:run
```

---

## 📝 Modelo de Datos

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | ID único |
| title | String | Título |
| author | String | Autor |
| publishedDate | LocalDate | Fecha publicación |
| category | String | Categoría |
| isbn | String | ISBN (único) |
| rating | Integer | Valoración (1-5) |
| visible | Boolean | Visibilidad |
| stock | Integer | Stock (>= 0) |

---

## 🧪 Pruebas Rápidas

### Crear un libro
```bash
curl -X POST http://localhost:8081/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Don Quijote",
    "author": "Miguel de Cervantes",
    "category": "Clásicos",
    "isbn": "978-84-376-0494-7",
    "rating": 5
  }'
```

### Health Check
```bash
curl http://localhost:8081/actuator/health
```

---

## ⚙️ Comandos Maven

```bash
# Compilar
./mvnw clean compile

# Ejecutar
./mvnw spring-boot:run

# Empaquetar
./mvnw clean package

# Tests
./mvnw test
```

---

## 📁 Estructura

```
src/main/java/com/relatosDePapel/demo/
├── controller/      # REST API
├── service/         # Lógica de negocio
├── repository/      # JPA
├── entity/          # Modelos
├── dto/             # DTOs
├── mapper/          # Entity ↔ DTO
├── specification/   # Búsquedas
└── exception/       # Errores
```

---

## 🔒 Seguridad

- No commitear `.env` (ya está en `.gitignore`)
- Usar `.env.example` como plantilla
- Variables de entorno para credenciales

---

**Autor:** Diego Arévalo | UNIR 2026
