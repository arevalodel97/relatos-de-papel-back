package com.relatosDePapel.payments.config;

import com.relatosDePapel.payments.interceptor.RequestLoggingInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/payments/**")
                .excludePathPatterns("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-books-payments API - Relatos de Papel")
                        .version("1.0.0")
                        .description("API REST para la gestión de compras de libros. " +
                                "Valida existencia, visibilidad y stock consultando a ms-books-catalogue vía Eureka.")
                        .contact(new Contact()
                                .name("Diego Arévalo")
                                .email("diego@relatosdepapel.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082")
                                .description("Servidor de desarrollo local")
                ));
    }
}
