package com.relatosDePapel.relatosservice.service;

import com.relatosDePapel.relatosservice.dto.*;

import java.util.List;

/**
 * Servicio para la gestión de libros
 */
public interface BookService {

    /**
     * Listar todos los libros (solo visibles por defecto)
     */
    List<BookResponseDTO> getAllBooks();

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

    /**
     * Buscar libros con filtros combinados (0..N parámetros)
     */
    List<BookResponseDTO> searchBooks(BookSearchParamsDTO params);
}
