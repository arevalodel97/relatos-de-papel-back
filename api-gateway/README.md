# API Gateway - Relatos de Papel

## 📋 Descripción

API Gateway construido con **Spring Cloud Gateway** que actúa como punto de entrada único para el backend de Relatos de Papel. 

### Características principales:

✅ **Punto de entrada único**: `POST /api/gateway`  
✅ **Enrutamiento dinámico**: Usa Eureka para descubrir servicios (sin IPs/puertos fijos)  
✅ **Transformación de métodos**: Recibe POST del frontend y lo transforma a GET/POST/PUT/PATCH/DELETE  
✅ **Load Balancing**: Balanceo automático cuando hay múltiples instancias del mismo servicio  
✅ **Reintentos automáticos**: 3 intentos con backoff exponencial en caso de fallos  

---

## 🚀 Inicio Rápido

### Prerrequisitos

1. **Eureka Server** debe estar corriendo en `http://localhost:8761`
2. **Java 17+** instalado
3. **Maven 3.6+** (o usar el wrapper incluido)

### Ejecutar el Gateway

```bash
# Opción 1: Con Maven Wrapper
./mvnw spring-boot:run

# Opción 2: Con Maven instalado
mvn spring-boot:run
```

El gateway estará disponible en: **http://localhost:8080**

---

## 📡 Endpoint Principal

### **POST /api/gateway**

Este es el **único endpoint** que debe usar el frontend.

#### Request Body

```json
{
  "method": "GET|POST|PUT|PATCH|DELETE",
  "queryParams": {
    "path": "/ruta/destino",
    "param1": "valor1",
    "param2": "valor2"
  },
  "body": {
    // Opcional: solo para POST, PUT, PATCH
  }
}
```

#### Campos Obligatorios

| Campo | Tipo | Descripción | Obligatorio |
|-------|------|-------------|-------------|
| `method` | String | Método HTTP: `GET`, `POST`, `PUT`, `PATCH`, `DELETE` | ✅ Sí |
| `queryParams` | Object | Debe incluir `path` + otros params opcionales | ✅ Sí |
| `queryParams.path` | String | Ruta destino (ej: `/books/search`) | ✅ Sí |
| `body` | Object | Cuerpo del request (solo POST/PUT/PATCH) | ❌ No |

---

## 🎯 Servicios Destino

El gateway enruta automáticamente según el `path`:

| Path Prefix | Servicio Destino | Puerto | Descripción |
|-------------|------------------|--------|-------------|
| `/books*` | `ms-books-catalogue` | 8081 | Catálogo de libros |
| `/payments*` | `ms-books-payments` | 8082 | Pagos y compras |

---

## 📝 Ejemplos de Uso

### 1. GET - Listar todos los libros

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "GET",
    "queryParams": {
      "path": "/books"
    }
  }'
```

**Se transforma internamente en:**
```
GET lb://ms-books-catalogue/books
```

---

### 2. GET - Buscar libros por autor

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "GET",
    "queryParams": {
      "path": "/books/search",
      "author": "Borges",
      "rating": "5"
    }
  }'
```

**Se transforma internamente en:**
```
GET lb://ms-books-catalogue/books/search?author=Borges&rating=5
```

---

### 3. GET - Obtener libro por ID

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "GET",
    "queryParams": {
      "path": "/books/1"
    }
  }'
```

**Se transforma internamente en:**
```
GET lb://ms-books-catalogue/books/1
```

---

### 4. POST - Crear un libro

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "POST",
    "queryParams": {
      "path": "/books"
    },
    "body": {
      "title": "El Aleph",
      "author": "Jorge Luis Borges",
      "isbn": "978-0142437889",
      "publishedDate": "1949-06-25",
      "category": "Ficción",
      "price": 19.99,
      "stock": 50,
      "visible": true,
      "description": "Colección de cuentos cortos"
    }
  }'
```

**Se transforma internamente en:**
```
POST lb://ms-books-catalogue/books
Body: { "title": "El Aleph", ... }
```

---

### 5. PUT - Actualizar libro completo

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "PUT",
    "queryParams": {
      "path": "/books/1"
    },
    "body": {
      "title": "El Aleph - Edición Actualizada",
      "author": "Jorge Luis Borges",
      "isbn": "978-0142437889",
      "publishedDate": "1949-06-25",
      "category": "Ficción",
      "price": 24.99,
      "stock": 100,
      "visible": true,
      "description": "Nueva edición con prólogo"
    }
  }'
```

---

### 6. PATCH - Actualizar libro parcialmente

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "PATCH",
    "queryParams": {
      "path": "/books/1"
    },
    "body": {
      "price": 29.99,
      "stock": 150
    }
  }'
```

---

### 7. DELETE - Eliminar libro

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "DELETE",
    "queryParams": {
      "path": "/books/1"
    }
  }'
```

---

### 8. POST - Crear una compra

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "POST",
    "queryParams": {
      "path": "/payments/purchases"
    },
    "body": {
      "customerName": "Juan Pérez",
      "customerEmail": "juan@example.com",
      "items": [
        {
          "bookId": 1,
          "quantity": 2
        },
        {
          "bookId": 2,
          "quantity": 1
        }
      ]
    }
  }'
```

---

### 9. GET - Listar todas las compras

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "GET",
    "queryParams": {
      "path": "/payments/purchases"
    }
  }'
```

---

### 10. GET - Obtener compra por ID

```bash
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "GET",
    "queryParams": {
      "path": "/payments/purchases/1"
    }
  }'
```

---

## ⚙️ Configuración

### application.yaml

```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

gateway:
  services:
    catalogue: ms-books-catalogue
    payments: ms-books-payments
```

### Variables de Entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Puerto del gateway | `8080` |
| `EUREKA_URL` | URL de Eureka Server | `http://localhost:8761/eureka/` |
| `CATALOGUE_SERVICE_NAME` | Nombre del servicio de catálogo | `ms-books-catalogue` |
| `PAYMENTS_SERVICE_NAME` | Nombre del servicio de pagos | `ms-books-payments` |

---

## 🔍 Monitoreo

### Health Check

```bash
# Gateway health
curl http://localhost:8080/api/health

# Actuator health
curl http://localhost:8080/actuator/health
```

### Métricas

```bash
curl http://localhost:8080/actuator/metrics
```

### Rutas del Gateway

```bash
curl http://localhost:8080/actuator/gateway/routes
```

---

## 🛡️ Manejo de Errores

### Validación de Request

Si falta un campo obligatorio:

```json
{
  "timestamp": "2026-02-05T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "validationErrors": {
    "method": "El campo 'method' es obligatorio"
  }
}
```

### Path Inválido

Si el path no empieza con `/books` o `/payments`:

```json
{
  "timestamp": "2026-02-05T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Path no válido. Debe comenzar con /books o /payments. Recibido: /invalid"
}
```

### Servicio No Disponible

Si el microservicio destino no está disponible, el gateway reintentará automáticamente **3 veces** con backoff exponencial (50ms, 100ms, 500ms).

---

## 🏗️ Arquitectura

```
┌─────────────┐
│  Frontend   │
└──────┬──────┘
       │ POST /api/gateway
       ▼
┌─────────────────────────┐
│    API Gateway          │
│    (Puerto 8080)        │
└─────────┬───────────────┘
          │
          ├──────────────────┐
          │                  │
          ▼                  ▼
┌──────────────────┐  ┌──────────────────┐
│  Eureka Server   │  │  Eureka Server   │
│  (Puerto 8761)   │  │  (Puerto 8761)   │
└────────┬─────────┘  └────────┬─────────┘
         │                     │
    ┌────┴────────┐       ┌────┴─────────┐
    ▼             ▼       ▼              ▼
┌─────────┐  ┌─────────┐ ┌──────────┐ ┌──────────┐
│ Catalogue│  │Catalogue│ │ Payments │ │ Payments │
│ Instance1│  │Instance2│ │Instance1 │ │Instance2 │
│  :8081   │  │  :8083  │ │  :8082   │ │  :8084   │
└─────────┘  └─────────┘ └──────────┘ └──────────┘
```

**Load Balancing**: Si hay múltiples instancias del mismo servicio registradas en Eureka, el gateway automáticamente distribuye las peticiones entre ellas usando Round Robin.

---

## 🧪 Testing

### Verificar Registro en Eureka

1. Abrir: http://localhost:8761
2. Verificar que `API-GATEWAY` aparezca en la lista de servicios

### Probar Conectividad

```bash
# Test básico
curl -X POST http://localhost:8080/api/gateway \
  -H "Content-Type: application/json" \
  -d '{
    "method": "GET",
    "queryParams": {"path": "/books"}
  }'
```

---

## 📦 Dependencias Principales

- **Spring Cloud Gateway** 2023.0.0
- **Spring Cloud Netflix Eureka Client** 2023.0.0
- **Spring Boot** 3.2.0
- **Lombok** (para reducir boilerplate)
- **Validation API** (para validar DTOs)

---

## 🚨 Troubleshooting

### El gateway no se registra en Eureka

Verificar que Eureka Server esté corriendo:
```bash
curl http://localhost:8761/actuator/health
```

### Error "Service not found"

1. Verificar que los microservicios estén registrados en Eureka
2. Verificar nombres en `application.yaml` (`ms-books-catalogue`, `ms-books-payments`)

### Timeout en requests

Aumentar timeout en `application.yaml`:
```yaml
spring:
  cloud:
    gateway:
      httpclient:
        connect-timeout: 3000
        response-timeout: 5s
```

---

## 📚 Recursos

- [Spring Cloud Gateway Docs](https://spring.io/projects/spring-cloud-gateway)
- [Netflix Eureka](https://github.com/Netflix/eureka)
- [WebClient Reference](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)

---

## 👨‍💻 Autor

Proyecto **Relatos de Papel** - UNIR

---

## 📄 Licencia

Este proyecto es parte de un trabajo académico.
