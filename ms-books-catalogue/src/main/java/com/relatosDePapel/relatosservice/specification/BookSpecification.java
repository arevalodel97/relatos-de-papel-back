package com.relatosDePapel.relatosservice.specification;

import com.relatosDePapel.relatosservice.dto.BookSearchParamsDTO;
import com.relatosDePapel.relatosservice.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> withFilters(BookSearchParamsDTO params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por título (búsqueda parcial, case insensitive)
            if (params.getTitle() != null && !params.getTitle().isBlank()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + params.getTitle().toLowerCase() + "%"
                ));
            }

            // Filtro por autor (búsqueda parcial, case insensitive)
            if (params.getAuthor() != null && !params.getAuthor().isBlank()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("author")),
                    "%" + params.getAuthor().toLowerCase() + "%"
                ));
            }

            // Filtro por fecha de publicación (exacta)
            if (params.getPublishedDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("publishedDate"), params.getPublishedDate()));
            }

            // Filtro por categoría (búsqueda parcial, case insensitive)
            if (params.getCategory() != null && !params.getCategory().isBlank()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("category")),
                    "%" + params.getCategory().toLowerCase() + "%"
                ));
            }

            // Filtro por ISBN (exacto)
            if (params.getIsbn() != null && !params.getIsbn().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("isbn"), params.getIsbn()));
            }

            // Filtro por rating (exacto)
            if (params.getRating() != null) {
                predicates.add(criteriaBuilder.equal(root.get("rating"), params.getRating()));
            }

            // Filtro por visible (por defecto true si no se especifica)
            if (params.getVisible() != null) {
                predicates.add(criteriaBuilder.equal(root.get("visible"), params.getVisible()));
            } else {
                // Si no se especifica, solo mostrar libros visibles
                predicates.add(criteriaBuilder.equal(root.get("visible"), true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
