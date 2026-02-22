package com.relatosDePapel.relatosservice.service;

import com.relatosDePapel.relatosservice.dto.*;

/**
 * Servicio para la gestión de libros
 */
public interface BookService {

    /** GET /books sin filtros → todos los visibles + facets globales */
    BookSearchResponseDTO getAllBooks();

    /**
     * Buscar libro por ID
     */
    BookResponseDTO getBookById(Long id);

    /**
     * Crear un nuevo libro
     */
    BookResponseDTO createBook(BookCreateRequestDTO dto);

    /**
     * Actualizar un libro completo (PUT)
     */
    BookResponseDTO updateBook(Long id, BookUpdateRequestDTO dto);

    /**
     * Actualizar un libro parcialmente (PATCH)
     */
    BookResponseDTO patchBook(Long id, BookPatchRequestDTO dto);

    /**
     * Eliminar un libro
     */
    void deleteBook(Long id);

    /** GET /books con filtros o GET /books/search → libros filtrados + facets de esos resultados */
    BookSearchResponseDTO searchBooks(BookSearchParamsDTO params);

    BookFacetsDTO getFacets();
}
