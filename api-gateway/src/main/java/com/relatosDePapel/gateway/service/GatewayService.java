package com.relatosDePapel.gateway.service;

import com.relatosDePapel.gateway.dto.GatewayRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Servicio que maneja el enrutamiento dinámico a través del gateway
 */
@Service
public class GatewayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayService.class);

    private final WebClient.Builder webClientBuilder;

    @Value("${gateway.services.catalogue}")
    private String catalogueServiceName;

    @Value("${gateway.services.payments}")
    private String paymentsServiceName;

    public GatewayService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Procesa el request del frontend y lo enruta al microservicio correspondiente
     *
     * @param requestDTO DTO con method, queryParams y body
     * @return Mono con la respuesta del microservicio destino
     */
    public Mono<String> routeRequest(GatewayRequestDTO requestDTO) {
        // 1. Validar que queryParams contenga 'path'
        String path = requestDTO.getQueryParams().get("path");
        if (path == null || path.isBlank()) {
            return Mono.error(new IllegalArgumentException(
                "El campo 'path' es obligatorio dentro de 'queryParams'"));
        }

        // 2. Determinar el servicio destino según el path
        String targetService = determineTargetService(path);

        // 3. Construir la URL completa con load balancing (lb://)
        String fullUrl = buildFullUrl(targetService, path, requestDTO.getQueryParams());

        // 4. Obtener el método HTTP
        HttpMethod httpMethod = HttpMethod.valueOf(requestDTO.getMethod());

        log.info("Enrutando request: {} {} -> {}", httpMethod, path, fullUrl);

        // 5. Construir y ejecutar la petición
        return executeRequest(fullUrl, httpMethod, requestDTO.getBody());
    }

    /**
     * Determina el servicio destino basado en el path
     */
    private String determineTargetService(String path) {
        if (path.startsWith("/books")) {
            return catalogueServiceName;
        } else if (path.startsWith("/payments")) {
            return paymentsServiceName;
        } else {
            throw new IllegalArgumentException(
                "Path no válido. Debe comenzar con /books o /payments. Recibido: " + path);
        }
    }

    /**
     * Construye la URL completa con load balancing y query params
     */
    private String buildFullUrl(String serviceName, String path, Map<String, String> queryParams) {
        // Usar lb:// para que Spring Cloud Gateway use el LoadBalancer de Eureka
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromUriString("lb://" + serviceName + path);

        // Agregar todos los queryParams excepto 'path'
        queryParams.forEach((key, value) -> {
            if (!"path".equals(key) && value != null && !value.isBlank()) {
                builder.queryParam(key, value);
            }
        });

        return builder.toUriString();
    }

    /**
     * Ejecuta el request HTTP usando WebClient reactivo
     */
    private Mono<String> executeRequest(String url, HttpMethod method, Object body) {
        WebClient webClient = webClientBuilder.build();

        WebClient.RequestBodySpec requestSpec = webClient
            .method(method)
            .uri(url)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        // Solo incluir body para POST, PUT, PATCH
        if (body != null && (method == HttpMethod.POST ||
                             method == HttpMethod.PUT ||
                             method == HttpMethod.PATCH)) {
            return requestSpec
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.debug("Respuesta recibida: {}", response))
                .doOnError(error -> log.error("Error en request: {}", error.getMessage()));
        } else {
            return requestSpec
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.debug("Respuesta recibida: {}", response))
                .doOnError(error -> log.error("Error en request: {}", error.getMessage()));
        }
    }
}
