package com.relatosDePapel.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Map;

/**
 * DTO para recibir requests del frontend en el endpoint POST /api/gateway
 */
public class GatewayRequestDTO {

    /**
     * Método HTTP real a ejecutar internamente
     * Valores permitidos: GET, POST, PUT, PATCH, DELETE
     */
    @NotBlank(message = "El campo 'method' es obligatorio")
    @Pattern(regexp = "^(GET|POST|PUT|PATCH|DELETE)$",
             message = "El método debe ser uno de: GET, POST, PUT, PATCH, DELETE")
    private String method;

    /**
     * Query params que DEBE incluir:
     * - path: ruta destino interna (obligatorio)
     * - resto de claves: se convierten en query string
     *
     * Ejemplo:
     * {
     *   "path": "/books/search",
     *   "author": "Borges",
     *   "rating": "5"
     * }
     *
     * Se transforma en: /books/search?author=Borges&rating=5
     */
    @NotNull(message = "El campo 'queryParams' es obligatorio")
    private Map<String, String> queryParams;

    /**
     * Body del request (opcional)
     * Solo se usa para POST, PUT, PATCH
     * Para GET y DELETE se ignora
     */
    private Object body;

    public GatewayRequestDTO() {
    }

    public GatewayRequestDTO(String method, Map<String, String> queryParams, Object body) {
        this.method = method;
        this.queryParams = queryParams;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
