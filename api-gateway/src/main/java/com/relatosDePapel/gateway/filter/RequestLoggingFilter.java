package com.relatosDePapel.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        long startTime = System.currentTimeMillis();

        // Log de entrada
        log.info("🔵 [GATEWAY REQUEST] {} {} | Query: {} | From: {}",
            request.getMethod(),
            request.getPath(),
            request.getURI().getQuery() != null ? request.getURI().getQuery() : "none",
            request.getRemoteAddress()
        );

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;

            // Log de salida
            log.info("🟢 [GATEWAY RESPONSE] {} {} | Status: {} | Duration: {}ms",
                request.getMethod(),
                request.getPath(),
                exchange.getResponse().getStatusCode(),
                duration
            );
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
