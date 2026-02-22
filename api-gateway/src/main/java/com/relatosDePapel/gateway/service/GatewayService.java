package com.relatosDePapel.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relatosDePapel.gateway.dto.GatewayRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GatewayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayService.class);

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${gateway.services.catalogue}")
    private String catalogueServiceName;

    @Value("${gateway.services.payments}")
    private String paymentsServiceName;

    public GatewayService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    public Mono<String> routeRequest(GatewayRequestDTO requestDTO) {
        String path = requestDTO.getQueryParams().get("path");
        if (path == null || path.isBlank()) {
            return Mono.error(new IllegalArgumentException(
                "El campo 'path' es obligatorio dentro de 'queryParams'"));
        }

        String targetService = determineTargetService(path);
        HttpMethod httpMethod = HttpMethod.valueOf(requestDTO.getMethod());

        log.info("🔀 [GATEWAY ROUTE] {} {} → {}", httpMethod, path, targetService);

        if (requestDTO.getBody() != null) {
            try {
                String bodyJson = objectMapper.writeValueAsString(requestDTO.getBody());
                log.info("📦 [GATEWAY BODY] {}", bodyJson);
            } catch (Exception e) {
                log.warn("No se pudo serializar el body: {}", e.getMessage());
            }
        }

        return executeRequest(targetService, path, requestDTO.getQueryParams(), httpMethod, requestDTO.getBody());
    }

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

    private Mono<String> executeRequest(String serviceName, String path, Map<String, String> queryParams,
                                        HttpMethod method, Object body) {
        WebClient webClient = webClientBuilder.build();

        String baseUri = "lb://" + serviceName + path;

        log.info("🌐 [GATEWAY] URI: {}", baseUri);
        log.info("🔍 [GATEWAY] QueryParams (sin 'path'): {}",
            queryParams.entrySet().stream()
                .filter(e -> !"path".equals(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        WebClient.RequestBodySpec requestSpec = webClient
            .method(method)
            .uri(baseUri, uriBuilder -> {
                queryParams.forEach((key, value) -> {
                    if (!"path".equals(key) && value != null && !value.isBlank()) {
                        uriBuilder.queryParam(key, value);
                    }
                });
                return uriBuilder.build();
            })
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        if (body != null && (method == HttpMethod.POST ||
                             method == HttpMethod.PUT ||
                             method == HttpMethod.PATCH)) {
            return requestSpec
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    log.info("✅ [GATEWAY RESPONSE] Status: 200 OK");
                    log.debug("Response: {}", response);
                })
                .onErrorResume(this::handleError);
        } else {
            return requestSpec
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    log.info("✅ [GATEWAY RESPONSE] Status: 200 OK");
                    log.debug("Response: {}", response);
                })
                .onErrorResume(this::handleError);
        }
    }

    private Mono<String> handleError(Throwable error) {
        if (error instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) error;
            int status = wcre.getStatusCode().value();
            String errorBody = wcre.getResponseBodyAsString();

            log.error("❌ [GATEWAY ERROR] Status: {} | Body: {}", status, errorBody);

            return Mono.just(errorBody);
        } else {
            log.error("❌ [GATEWAY ERROR] Tipo desconocido: {}", error.getClass().getSimpleName(), error);
            return Mono.error(error);
        }
    }
}
