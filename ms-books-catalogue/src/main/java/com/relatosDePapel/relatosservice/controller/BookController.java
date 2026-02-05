package com.relatosDePapel.relatosservice.controller;

import com.relatosDePapel.relatosservice.dto.*;
import com.relatosDePapel.relatosservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "API de gestión del catálogo de libros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Listar todos los libros o filtrar", description = "Obtiene la lista de todos los libros visibles o filtra por parámetros")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de libros obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean visible) {

        BookSearchParamsDTO params = new BookSearchParamsDTO();
        params.setTitle(title);
        params.setAuthor(author);
        params.setPublishedDate(publishedDate);
        params.setCategory(category);
        params.setIsbn(isbn);
        params.setRating(rating);
        params.setVisible(visible);

        // Si no se envía ningún filtro, obtener todos visibles (comportamiento por defecto)
        boolean hasAnyFilter = (title != null && !title.isBlank()) ||
                (author != null && !author.isBlank()) ||
                (publishedDate != null) ||
                (category != null && !category.isBlank()) ||
                (isbn != null && !isbn.isBlank()) ||
                (rating != null) ||
                (visible != null);

        List<BookResponseDTO> books;
        if (!hasAnyFilter) {
            books = bookService.getAllBooks();
        } else {
            books = bookService.searchBooks(params);
        }
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Obtener libro por ID", description = "Obtiene los detalles de un libro específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @Parameter(description = "ID del libro", required = true) @PathVariable Long id) {
        BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Crear un libro", description = "Crea un nuevo libro en el catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "El ISBN ya existe")
    })
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookCreateRequestDTO dto) {
        BookResponseDTO book = bookService.createBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    /**
     * PUT /books/{id} - Actualizar un libro completo
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequestDTO dto) {
        BookResponseDTO book = bookService.updateBook(id, dto);
        return ResponseEntity.ok(book);
    }

    /**
     * PATCH /books/{id} - Actualizar un libro parcialmente
     */
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> patchBook(
            @PathVariable Long id,
            @Valid @RequestBody BookPatchRequestDTO dto) {
        BookResponseDTO book = bookService.patchBook(id, dto);
        return ResponseEntity.ok(book);
    }

    /**
     * DELETE /books/{id} - Eliminar un libro
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /books/search - Buscar libros con filtros combinados (0..N parámetros)
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean visible) {

        BookSearchParamsDTO params = new BookSearchParamsDTO();
        params.setTitle(title);
        params.setAuthor(author);
        params.setPublishedDate(publishedDate);
        params.setCategory(category);
        params.setIsbn(isbn);
        params.setRating(rating);
        params.setVisible(visible);

        List<BookResponseDTO> books = bookService.searchBooks(params);
        return ResponseEntity.ok(books);
    }
}
