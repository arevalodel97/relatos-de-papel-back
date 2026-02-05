# Eureka Server

Servidor de registro y descubrimiento de servicios para la arquitectura de microservicios.

## Puerto
- **8761** (puerto por defecto de Eureka)

## URL de acceso
- Dashboard: http://localhost:8761
- API: http://localhost:8761/eureka/

## Ejecutar

```bash
./mvnw spring-boot:run
```

## Servicios registrados

Una vez en funcionamiento, los siguientes servicios se registrarán automáticamente:
- ms-books-catalogue (puerto 8081)
- ms-books-payments (puerto 8082)

## Verificar estado

```bash
curl http://localhost:8761/actuator/health
```
