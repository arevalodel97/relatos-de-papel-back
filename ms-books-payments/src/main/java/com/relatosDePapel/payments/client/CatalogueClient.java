package com.relatosDePapel.payments.client;

import com.relatosDePapel.payments.dto.BookDTO;
import com.relatosDePapel.payments.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente HTTP para comunicarse con ms-books-catalogue vía Eureka
 * SIN hardcodear IP/puerto
 */
@Component
public class CatalogueClient {

    private final RestTemplate restTemplate;
    private final String catalogueServiceName;

    public CatalogueClient(RestTemplate restTemplate,
                          @Value("${catalogue.service.name}") String catalogueServiceName) {
        this.restTemplate = restTemplate;
        this.catalogueServiceName = catalogueServiceName;
    }

    /**
     * Obtiene un libro por ID desde ms-books-catalogue
     * Usa el nombre del servicio registrado en Eureka (sin IP/puerto)
     * Ahora además admite que la propiedad sea una URL completa (http://host:port)
     */
    public BookDTO getBookById(Long bookId) {
        // Si el valor configurado comienza con http(s):// lo usamos tal cual (modo directo, sin Eureka)
        final String url;
        if (catalogueServiceName != null && (catalogueServiceName.startsWith("http://") || catalogueServiceName.startsWith("https://"))) {
            url = String.format("%s/books/%d", catalogueServiceName, bookId);
        } else {
            // Modo Eureka: el RestTemplate @LoadBalanced resolverá el nombre del servicio
            url = String.format("http://%s/books/%d", catalogueServiceName, bookId);
        }

        try {
            return restTemplate.getForObject(url, BookDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new BookNotFoundException(bookId);
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con ms-books-catalogue: " + e.getMessage(), e);
        }
    }
}
