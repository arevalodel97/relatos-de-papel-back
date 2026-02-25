package com.relatosDePapel.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración CORS para Spring Cloud Gateway (WebFlux).
 *
 * Permite:
 *  - El origen del frontend en local/staging  → FRONTEND_URL
 *  - Segunda URL opcional                     → FRONTEND_URL_2
 *  - Cualquier preview/producción de Vercel   → *.vercel.app
 *  - Dominio custom de Vercel (opcional)      → VERCEL_CUSTOM_DOMAIN
 *
 * Configurar estas variables en el dashboard de Railway.
 */
@Configuration
public class CorsConfig {

    /** URL del front en local o en producción, ej: https://mi-app.vercel.app */
    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;

    /** Segunda URL opcional (otro entorno, ej: localhost:3000) */
    @Value("${FRONTEND_URL_2:http://localhost:3000}")
    private String frontendUrl2;

    /**
     * Dominio custom si tienes uno propio en Vercel, ej: https://mi-tienda.com
     * Deja en blanco si no lo necesitas.
     */
    @Value("${VERCEL_CUSTOM_DOMAIN:}")
    private String vercelCustomDomain;

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ── Orígenes permitidos con patrones ────────────────────────────────
        // addAllowedOriginPattern soporta wildcards → cubre *.vercel.app
        config.addAllowedOriginPattern("https://*.vercel.app");  // todos los previews y prod de Vercel
        config.addAllowedOriginPattern(frontendUrl);
        config.addAllowedOriginPattern(frontendUrl2);

        if (vercelCustomDomain != null && !vercelCustomDomain.isBlank()) {
            config.addAllowedOriginPattern(vercelCustomDomain);
        }

        // ── Métodos HTTP ─────────────────────────────────────────────────────
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        // ── Headers ──────────────────────────────────────────────────────────
        config.addAllowedHeader("*");
        config.addExposedHeader(HttpHeaders.AUTHORIZATION);
        config.addExposedHeader(HttpHeaders.CONTENT_TYPE);

        // ── Credentials y caché preflight ────────────────────────────────────
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
