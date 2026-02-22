# 📚 Relatos de Papel - Backend Microservicios
Sistema backend de microservicios para la plataforma de venta de libros **Relatos de Papel**.
## 🚀 Tecnologías
- **Java:** 25
- **Spring Boot:** 4.0.2
- **Spring Cloud:** 2024.0.0
## 📦 Microservicios
| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| eureka-server | 8761 | Service Discovery |
| api-gateway | 8080 | Gateway unificado |
| ms-books-catalogue | 8081 | Gestión de libros |
| ms-books-payments | 8082 | Gestión de pagos |
## ⚙️ Instalación
```bash
# 1. Verificar Java 25
java -version
# 2. Compilar todos los proyectos
cd eureka-server && ./mvnw clean install -DskipTests && cd ..
cd api-gateway && ./mvnw clean install -DskipTests && cd ..
cd ms-books-catalogue && ./mvnw clean install -DskipTests && cd ..
cd ms-books-payments && ./mvnw clean install -DskipTests && cd ..
# 3. Iniciar servicios (en orden)
cd eureka-server && ./mvnw spring-boot:run  # Terminal 1
cd ms-books-catalogue && ./mvnw spring-boot:run  # Terminal 2
cd ms-books-payments && ./mvnw spring-boot:run  # Terminal 3
cd api-gateway && ./mvnw spring-boot:run  # Terminal 4
```
## 📖 Documentación
Ver [VERSION-UPDATE.md](VERSION-UPDATE.md) para detalles de actualización a Java 25 y Spring Boot 4.0.2.
