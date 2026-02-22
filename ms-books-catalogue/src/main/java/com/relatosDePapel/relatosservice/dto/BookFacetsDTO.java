package com.relatosDePapel.relatosservice.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO que representa los facets (agregaciones) de la búsqueda de libros.
 * Permite al frontend construir filtros dinámicos con los valores disponibles.
 */
public class BookFacetsDTO {

    /** Categorías disponibles con su conteo */
    private List<FacetBucket> categories;

    /** Ratings disponibles con su conteo */
    private List<FacetBucket> ratings;

    /** Rango de precios (min/max) */
    private PriceRange priceRange;

    /** Si hay libros con stock > 0 y cuántos */
    private Long booksWithStock;

    public BookFacetsDTO() {}

    public List<FacetBucket> getCategories() { return categories; }
    public void setCategories(List<FacetBucket> categories) { this.categories = categories; }

    public List<FacetBucket> getRatings() { return ratings; }
    public void setRatings(List<FacetBucket> ratings) { this.ratings = ratings; }

    public PriceRange getPriceRange() { return priceRange; }
    public void setPriceRange(PriceRange priceRange) { this.priceRange = priceRange; }

    public Long getBooksWithStock() { return booksWithStock; }
    public void setBooksWithStock(Long booksWithStock) { this.booksWithStock = booksWithStock; }

    // ─── Clases internas ────────────────────────────────────────────────────────

    public static class FacetBucket {
        private String key;
        private Long count;

        public FacetBucket() {}
        public FacetBucket(String key, Long count) {
            this.key = key;
            this.count = count;
        }

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }

        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public static class PriceRange {
        private Double min;
        private Double max;

        public PriceRange() {}
        public PriceRange(Double min, Double max) {
            this.min = min;
            this.max = max;
        }

        public Double getMin() { return min; }
        public void setMin(Double min) { this.min = min; }

        public Double getMax() { return max; }
        public void setMax(Double max) { this.max = max; }
    }
}
