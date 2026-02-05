package com.relatosDePapel.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración del WebClient con Load Balancing
 * Permite usar lb:// en las URLs para balanceo automático vía Eureka
 */
@Configuration
public class WebClientConfig {

    /**
     * WebClient con Load Balancer habilitado
     * Esto permite resolver nombres de servicio (lb://ms-books-catalogue)
     * a instancias reales registradas en Eureka
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
