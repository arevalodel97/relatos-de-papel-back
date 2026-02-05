package com.relatosDePapel.gateway.controller;

import com.relatosDePapel.gateway.dto.GatewayRequestDTO;
import com.relatosDePapel.gateway.service.GatewayService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controlador principal del API Gateway
 * Expone un único endpoint POST /api/gateway que recibe todas las peticiones del frontend
 */
@RestController
@RequestMapping("/api")
public class GatewayController {

    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    /**
     * Endpoint único del gateway
     * Recibe SIEMPRE un POST del frontend y lo transforma internamente
     * en GET/POST/PUT/PATCH/DELETE según el campo 'method'
     *
     * @param requestDTO DTO con method, queryParams (incluyendo path) y body opcional
     * @return Respuesta del microservicio destino
     */
    @PostMapping(value = "/gateway",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> handleGatewayRequest(
            @Valid @RequestBody GatewayRequestDTO requestDTO) {

        log.info("📥 [GATEWAY] Request recibido: method={}, path={}",
                 requestDTO.getMethod(),
                 requestDTO.getQueryParams().get("path"));

        return gatewayService.routeRequest(requestDTO)
            .map(response -> {
                try {
                    // Intentar parsear como JSON para retornarlo correctamente
                    Object jsonResponse = new com.fasterxml.jackson.databind.ObjectMapper().readValue(response, Object.class);
                    return ResponseEntity.ok(jsonResponse);
                } catch (Exception e) {
                    // Si no es JSON, retornar como string
                    return ResponseEntity.ok((Object) response);
                }
            })
            .onErrorResume(error -> {
                log.error("❌ [GATEWAY ERROR] {}", error.getMessage(), error);

                // Retornar error en formato estandarizado
                return Mono.just(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((Object) new java.util.HashMap<String, String>() {{
                        put("error", "Internal Server Error");
                        put("message", error.getMessage());
                        put("timestamp", java.time.Instant.now().toString());
                        put("status", "500");
                    }}));
            });
    }

    /**
     * Endpoint de health check simple
     */
    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("{\"status\":\"UP\",\"service\":\"api-gateway\"}"));
    }
}
