# 💳 ms-books-payments - Microservicio de Pagos

Microservicio REST para el registro de compras de libros - "Relatos de Papel"

## 🎯 Características

- ✅ API REST para registrar compras de libros
- ✅ Validación de existencia, visibilidad y stock consultando a **ms-books-catalogue** vía **Eureka**
- ✅ Base de datos MySQL (independiente del catálogo)
- ✅ Comunicación entre microservicios **SIN hardcodear IP/puerto**
- ✅ Documentación con Swagger/OpenAPI
- ✅ Validaciones de negocio (libros ocultos, stock insuficiente)
- ✅ Manejo de errores centralizado

---

## 🚀 Inicio Rápido

### Pre-requisitos

- **Java 25** (JDK)
- **Maven 3.6+**
- **MySQL 8+**
- **Eureka Server** corriendo en `http://localhost:8761`
- **ms-books-catalogue** corriendo y registrado en Eureka

### Configuración

1. Copia el archivo de ejemplo:
```bash
cp .env.example .env
```

2. Edita `.env` con tus credenciales:
```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=payments_db
DB_USERNAME=root
DB_PASSWORD=12345
SERVER_PORT=8082
EUREKA_ENABLED=true
EUREKA_URL=http://localhost:8761/eureka/
CATALOGUE_SERVICE_NAME=ms-books-catalogue
```

3. Crea la base de datos:
```bash
mysql -u root -p
CREATE DATABASE payments_db;
EXIT;
```

4. Ejecuta el microservicio:
```bash
./mvnw spring-boot:run
```

**¡Listo!** El microservicio estará disponible en `http://localhost:8082`

---

## 📖 Documentación API

### Swagger UI
```
http://localhost:8082/swagger-ui.html
```

### OpenAPI JSON
```
http://localhost:8082/v3/api-docs
```

---

## 🌐 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/payments/purchases` | Registrar una compra |
| GET | `/payments/purchases` | Listar todas las compras |
| GET | `/payments/purchases/{id}` | Obtener compra por ID |

---

## 📝 Flujo de Negocio

### POST /payments/purchases

1. **Recibe la petición** con datos del cliente y lista de libros
2. **Por cada libro:**
   - Llama a `ms-books-catalogue` vía Eureka: `http://ms-books-catalogue/books/{id}`
   - Valida que exista (si no → 404)
   - Valida que esté visible (`visible=true`, si no → 409)
   - Valida que haya stock suficiente (si no → 409)
3. **Si todas las validaciones pasan:**
   - Crea la compra en la BD de `payments_db`
   - Retorna 201 Created con el ID de la compra

---

## 🧪 Ejemplo de Uso

### Crear una compra

```bash
curl -X POST http://localhost:8082/payments/purchases \
  -H "Content-Type: application/json" \
  -d '{
    "customer": {
      "name": "Juan Pérez",
      "email": "juan@example.com",
      "address": "Calle Principal 123"
    },
    "items": [
      {
        "bookId": 1,
        "quantity": 2
      },
      {
        "bookId": 3,
        "quantity": 1
      }
    ]
  }'
```

**Respuesta exitosa (201):**
```json
{
  "id": 1,
  "customer": {
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "address": "Calle Principal 123"
  },
  "items": [
    {
      "bookId": 1,
      "quantity": 2
    },
    {
      "bookId": 3,
      "quantity": 1
    }
  ],
  "status": "COMPLETED",
  "createdAt": "2026-02-04T20:30:00"
}
```

### Listar compras

```bash
curl http://localhost:8082/payments/purchases
```

### Obtener una compra

```bash
curl http://localhost:8082/payments/purchases/1
```

---

## 🔄 Comunicación con ms-books-catalogue

### ❌ INCORRECTO (hardcoded):
```java
String url = "http://localhost:8081/books/" + bookId;
```

### ✅ CORRECTO (vía Eureka):
```java
@LoadBalanced
RestTemplate restTemplate;

String url = "http://ms-books-catalogue/books/" + bookId;
restTemplate.getForObject(url, BookDTO.class);
```

El `@LoadBalanced` permite que Spring Cloud resuelva el nombre del servicio usando Eureka.

---

## 📊 Modelo de Datos

### Purchase (Compra)
```sql
CREATE TABLE purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL
);
```

### PurchaseItem (Items de la compra)
```sql
CREATE TABLE purchase_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES purchases(id)
);
```

---

## 🗂️ Estructura del Proyecto

```
src/main/java/com/relatosDePapel/payments/
├── MsBooksPaymentsApplication.java
├── controller/
│   └── PurchaseController.java
├── service/
│   ├── PurchaseService.java
│   └── PurchaseServiceImpl.java
├── repository/
│   └── PurchaseRepository.java
├── entity/
│   ├── Purchase.java
│   ├── PurchaseItem.java
│   └── Customer.java
├── dto/
│   ├── PurchaseCreateRequestDTO.java
│   ├── PurchaseResponseDTO.java
│   ├── PurchaseItemDTO.java
│   ├── CustomerDTO.java
│   └── BookDTO.java
├── mapper/
│   └── PurchaseMapper.java
├── client/
│   └── CatalogueClient.java
├── exception/
│   ├── BookNotFoundException.java
│   ├── BookNotVisibleException.java
│   ├── InsufficientStockException.java
│   ├── PurchaseNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
└── config/
    ├── RestTemplateConfig.java
    └── OpenApiConfig.java
```

---

## ⚙️ Comandos Maven

```bash
# Compilar
./mvnw clean compile

# Empaquetar
./mvnw clean package

# Ejecutar
./mvnw spring-boot:run

# Tests
./mvnw test
```

---

## 🔒 Seguridad

- No commitear el archivo `.env`
- Usar `.env.example` como plantilla
- Credenciales en variables de entorno

---

## 📡 Health Check

```bash
curl http://localhost:8082/actuator/health
```

---

## 🐛 Troubleshooting

### Error: "Cannot connect to Eureka"
- Verifica que Eureka Server esté corriendo en `http://localhost:8761`
- Revisa la configuración en `application.yaml`

### Error: "ms-books-catalogue not found"
- Verifica que `ms-books-catalogue` esté registrado en Eureka
- Abre `http://localhost:8761` y busca el servicio en la lista

### Error: "Book not found (404)"
- El libro no existe en el catálogo
- Verifica el ID del libro

### Error: "Conflict (409)"
- El libro no está visible (`visible=false`)
- O no hay stock suficiente

---

## 👨‍💻 Autor

**Diego Arévalo**  
Actividad 2 - Backend Aplicaciones Web - UNIR

---

## 📄 Licencia

Proyecto académico - UNIR 2026
