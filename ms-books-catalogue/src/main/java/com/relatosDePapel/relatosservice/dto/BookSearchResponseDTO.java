package com.relatosDePapel.relatosservice.dto;

import java.util.List;

/**
 * Respuesta unificada que incluye los libros encontrados y los facets calculados
 * sobre los mismos filtros aplicados, para que el frontend pueda construir
 * los filtros dinámicos sin hacer una llamada adicional.
 */
public class BookSearchResponseDTO {

    private List<BookResponseDTO> books;
    private BookFacetsDTO facets;

    // Metadatos de paginación
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public BookSearchResponseDTO() {}

    public BookSearchResponseDTO(List<BookResponseDTO> books, BookFacetsDTO facets,
                                  long totalElements, int currentPage, int pageSize) {
        this.books = books;
        this.facets = facets;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
    }

    public List<BookResponseDTO> getBooks() { return books; }
    public void setBooks(List<BookResponseDTO> books) { this.books = books; }

    public BookFacetsDTO getFacets() { return facets; }
    public void setFacets(BookFacetsDTO facets) { this.facets = facets; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
