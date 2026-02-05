# Colección de Postman - MS Books Payments

## 📋 Descripción

Esta colección contiene **todos los tests posibles** para el microservicio `ms-books-payments` que corre en el puerto **8082**.

## 🚀 Requisitos Previos

1. **MySQL** corriendo en `localhost:3306`
2. **ms-books-catalogue** corriendo en `localhost:8081` (necesario para validar libros)
3. **ms-books-payments** corriendo en `localhost:8082`

### Verificar que el servicio está corriendo:

```bash
curl http://localhost:8082/actuator/health
```

Deberías ver un JSON con `"status":"UP"`.

## 📁 Estructura de la Colección

La colección está organizada en **carpetas** según el tipo de operación:

### 0. Health Check (2 requests)
- ✅ Verificar estado del servicio
- ✅ Obtener documentación OpenAPI

### 1. GET Requests - Lectura Inicial (2 requests)
- ✅ Listar todas las compras (vacío)
- ✅ Obtener compra por ID inexistente (404)

### 2. POST Requests - Creación (11 requests)
**Casos exitosos:**
- ✅ Crear compra con 1 libro
- ✅ Crear compra con múltiples libros
- ✅ Crear compra con otro cliente

**Casos de error:**
- ❌ Libro no encontrado (404)
- ❌ Customer null (400)
- ❌ Email inválido (400)
- ❌ Items vacío (400)
- ❌ Cantidad negativa (400)
- ❌ Cantidad cero (400)
- ❌ Campos requeridos faltantes (400)

### 3. GET Requests - Verificación (5 requests)
- ✅ Listar todas las compras (con datos)
- ✅ Obtener compra por ID (usando variable)
- ✅ Obtener compra ID 1
- ✅ Obtener compra ID 2
- ✅ Obtener compra ID 3

## 📥 Importar la Colección en Postman

1. Abre **Postman**
2. Click en **Import**
3. Selecciona el archivo: `MS-Payments-Postman-Collection.json`
4. La colección se importará automáticamente

## ⚙️ Variables de Entorno

La colección incluye las siguientes variables:

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `baseUrl` | `http://localhost:8082` | URL base del servicio |
| `purchaseId` | `1` | ID de compra (se actualiza automáticamente) |

### Variable Automática

El request **"POST - Crear compra exitosa"** incluye un script que **guarda automáticamente** el `purchaseId` de la compra creada, para usarlo en requests posteriores.

## 🎯 Orden de Ejecución Recomendado

### Opción 1: Ejecución Manual

1. **Health Check** → Verificar que el servicio funciona
2. **GET Requests (Inicial)** → Ver estado inicial (vacío)
3. **POST Requests** → Crear compras (exitosos y errores)
4. **GET Requests (Verificación)** → Verificar datos creados

### Opción 2: Ejecución Automática (Collection Runner)

1. En Postman, haz click derecho en la colección
2. Selecciona **"Run collection"**
3. Asegúrate de ejecutar en el orden de las carpetas
4. Click en **"Run MS Books Payments - API Tests"**

## 📊 Respuestas Esperadas

### ✅ Casos Exitosos

#### GET /payments/purchases (vacío)
```json
[]
```

#### POST /payments/purchases (exitoso)
```json
{
  "id": 1,
  "customerId": 1,
  "customerName": "Juan Pérez",
  "customerEmail": "juan.perez@example.com",
  "customerAddress": "Calle Principal 123, Madrid",
  "purchaseDate": "2026-02-05T05:10:30.123456",
  "total": 39.98,
  "items": [
    {
      "id": 1,
      "bookId": 1,
      "bookTitle": "El Quijote",
      "bookAuthor": "Miguel de Cervantes",
      "bookIsbn": "978-1234567890",
      "quantity": 2,
      "unitPrice": 19.99,
      "subtotal": 39.98
    }
  ]
}
```

#### GET /payments/purchases/{id} (existente)
```json
{
  "id": 1,
  "customerId": 1,
  "customerName": "Juan Pérez",
  ...
}
```

### ❌ Casos de Error

#### 404 - Libro no encontrado
```json
{
  "error": "Not Found",
  "message": "Libro con ID 99999 no encontrado",
  "path": "/payments/purchases",
  "status": 404,
  "timestamp": "2026-02-05T05:15:20.123456",
  "validationErrors": null
}
```

#### 400 - Validación fallida
```json
{
  "error": "Bad Request",
  "message": "Error de validación",
  "path": "/payments/purchases",
  "status": 400,
  "timestamp": "2026-02-05T05:20:10.123456",
  "validationErrors": {
    "customer.email": "debe ser una dirección de correo electrónico con formato correcto",
    "items": "no debe estar vacío"
  }
}
```

#### 404 - Compra no encontrada
```json
{
  "error": "Not Found",
  "message": "Compra con ID 999 no encontrada",
  "path": "/payments/purchases/999",
  "status": 404,
  "timestamp": "2026-02-05T05:25:30.123456",
  "validationErrors": null
}
```

## 🔍 Endpoints Disponibles

| Método | Endpoint | Descripción | Código Esperado |
|--------|----------|-------------|-----------------|
| GET | `/actuator/health` | Health check | 200 |
| GET | `/v3/api-docs` | Documentación OpenAPI | 200 |
| GET | `/payments/purchases` | Listar todas las compras | 200 |
| GET | `/payments/purchases/{id}` | Obtener compra por ID | 200, 404 |
| POST | `/payments/purchases` | Crear nueva compra | 201, 400, 404 |

## 🛠️ Troubleshooting

### Error: Connection refused (ECONNREFUSED)

**Causa:** El servicio no está corriendo.

**Solución:**
```bash
cd /home/diego_arevalo/Escritorio/backend-unir/ms-books-payments
./mvnw spring-boot:run
```

### Error: 500 - No instances available for ms-books-catalogue

**Causa:** El servicio de catálogo no está disponible.

**Solución:**
```bash
# En otra terminal, arrancar ms-books-catalogue
cd /home/diego_arevalo/Escritorio/backend-unir/ms-books-catalogue
./mvnw spring-boot:run
```

### Error: 404 - Libro no encontrado

**Causa:** El libro no existe en la base de datos del catálogo.

**Solución:** Crear libros en `ms-books-catalogue` primero usando su API.

## 📝 Notas Adicionales

### Sobre los IDs de Libros

Los requests usan los siguientes IDs de libros:
- `bookId: 1` - Debe existir en ms-books-catalogue
- `bookId: 2` - Debe existir en ms-books-catalogue  
- `bookId: 3` - Debe existir en ms-books-catalogue
- `bookId: 99999` - No existe (para probar error 404)

### Sobre las Validaciones

El servicio valida:
- ✅ Existencia del libro en el catálogo
- ✅ Visibilidad del libro (`visible: true`)
- ✅ Stock suficiente (`stock >= quantity`)
- ✅ Formato de email válido
- ✅ Campos requeridos no vacíos
- ✅ Cantidades mayores a cero

## 📄 Swagger UI

También puedes probar los endpoints usando Swagger UI:

```
http://localhost:8082/swagger-ui.html
```

## 🎉 Total de Tests

La colección incluye **20 requests** que cubren:
- ✅ 2 Health checks
- ✅ 7 GET requests
- ✅ 11 POST requests (3 exitosos + 8 casos de error)

---

**Autor:** GitHub Copilot  
**Fecha:** 2026-02-05  
**Versión:** 1.0
