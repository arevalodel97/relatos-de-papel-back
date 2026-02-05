package com.relatosDePapel.relatosservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-books-catalogue API - Relatos de Papel")
                        .version("1.0.0")
                        .description("API REST para la gestión del catálogo de libros. " +
                                "Incluye operaciones CRUD completas y búsqueda dinámica por múltiples atributos.")
                        .contact(new Contact()
                                .name("Diego Arévalo")
                                .email("diego@relatosdepapel.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de desarrollo local")
                ));
    }
}
