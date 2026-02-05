package com.relatosDePapel.gateway.controller;

import com.relatosDePapel.gateway.dto.GatewayRequestDTO;
import com.relatosDePapel.gateway.service.GatewayService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public Mono<ResponseEntity<String>> handleGatewayRequest(
            @Valid @RequestBody GatewayRequestDTO requestDTO) {

        log.info("Request recibido en gateway: method={}, path={}",
                 requestDTO.getMethod(),
                 requestDTO.getQueryParams().get("path"));

        return gatewayService.routeRequest(requestDTO)
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error procesando request: {}", error.getMessage());
                return Mono.just(
                    ResponseEntity
                        .badRequest()
                        .body("{\"error\": \"" + error.getMessage() + "\"}")
                );
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
