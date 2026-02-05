# Configuración de Eureka Server y Client

## Estado Actual

✅ **Eureka Client ya está configurado y habilitado en este microservicio**

El microservicio `ms-books-catalogue` está configurado como cliente de Eureka y se registrará automáticamente cuando haya un servidor Eureka disponible.

## Configuración del Cliente (Ya implementada)

### Dependencias (pom.xml)
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### Aplicación Principal
```java
@SpringBootApplication
@EnableDiscoveryClient
public class RelatosDePapelApplication {
    // ...
}
```

### Configuración (application.yaml)
```yaml
spring:
  application:
    name: ms-books-catalogue

eureka:
  client:
    enabled: true
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    instance-id: ${spring.application.name}:${server.port}
```

## Cómo crear el Servidor Eureka

### Opción 1: Proyecto separado con Spring Initializr

1. Ve a https://start.spring.io/
2. Configura:
   - Project: Maven
   - Language: Java
   - Spring Boot: 4.0.2
   - Group: com.relatosDePapel
   - Artifact: eureka-server
   - Dependencies: Eureka Server
3. Genera y descarga el proyecto
4. Descomprime y abre el proyecto

### Opción 2: Crear proyecto manualmente

**pom.xml del servidor:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.2</version>
        <relativePath/>
    </parent>
    
    <groupId>com.relatosDePapel</groupId>
    <artifactId>eureka-server</artifactId>
    <version>1.0.0</version>
    <name>Eureka Server</name>
    
    <properties>
        <java.version>25</java.version>
        <spring-cloud.version>2025.1.0</spring-cloud.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

**Clase principal (EurekaServerApplication.java):**
```java
package com.relatosDePapel.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

**application.yml del servidor:**
```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    wait-time-in-ms-when-sync-empty: 0
    enable-self-preservation: false
```

## Ejecución

### 1. Iniciar el Servidor Eureka
```bash
cd eureka-server
./mvnw spring-boot:run
```

El servidor estará disponible en: http://localhost:8761

### 2. Iniciar el microservicio ms-books-catalogue
```bash
cd ms-books-catalogue
./mvnw spring-boot:run
```

El microservicio se registrará automáticamente en Eureka.

## Verificación

1. Abre el navegador en http://localhost:8761
2. Deberías ver el dashboard de Eureka
3. En "Instances currently registered with Eureka" deberías ver:
   - **MS-BOOKS-CATALOGUE** registrado

## Variables de Entorno (Opcional)

Puedes sobrescribir la configuración usando variables de entorno:

```bash
# Deshabilitar Eureka
export EUREKA_ENABLED=false

# Cambiar URL del servidor Eureka
export EUREKA_URL=http://eureka-server:8761/eureka/

# Ejecutar la aplicación
./mvnw spring-boot:run
```

## Docker Compose (Opcional)

Si deseas ejecutar Eureka y el microservicio con Docker:

```yaml
version: '3.8'
services:
  eureka-server:
    image: springcloud/eureka
    ports:
      - "8761:8761"
    environment:
      - SPRING_APPLICATION_NAME=eureka-server
  
  ms-books-catalogue:
    build: .
    ports:
      - "8081:8081"
    environment:
      - EUREKA_URL=http://eureka-server:8761/eureka/
      - DB_HOST=mysql
    depends_on:
      - eureka-server
      - mysql
  
  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=12345
      - MYSQL_DATABASE=books_catalogue_db
    ports:
      - "3306:3306"
```

## Troubleshooting

### El microservicio no se registra en Eureka

1. Verifica que el servidor Eureka esté ejecutándose en http://localhost:8761
2. Revisa los logs del microservicio para errores de conexión
3. Verifica que `eureka.client.enabled=true` en application.yaml

### Timeout al conectar con Eureka

- El microservicio intentará conectarse cada 30 segundos
- Asegúrate de que el servidor Eureka esté iniciado ANTES que el microservicio

### Cambiar puerto del servidor Eureka

Si necesitas usar otro puerto (ej: 8888):

**Servidor Eureka (application.yml):**
```yaml
server:
  port: 8888
```

**Cliente (application.yaml o variable de entorno):**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8888/eureka/
```

## Beneficios de Eureka

✅ **Service Discovery**: Los microservicios se encuentran automáticamente
✅ **Load Balancing**: Distribución de carga entre instancias
✅ **Health Checks**: Monitoreo automático del estado de los servicios
✅ **Failover**: Manejo de fallos automático
✅ **Escalabilidad**: Añade/elimina instancias dinámicamente
