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

    @Operation(summary = "Listar todos los libros o filtrar",
               description = "Obtiene la lista de todos los libros visibles. Acepta filtros opcionales.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de libros obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<BookSearchResponseDTO> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publicationDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        BookSearchParamsDTO params = new BookSearchParamsDTO();
        params.setTitle(title);
        params.setAuthor(author);
        params.setPublicationDate(publicationDate);
        params.setCategory(category);
        params.setIsbn(isbn);
        params.setRating(rating);
        params.setVisible(visible);
        params.setInStock(inStock);
        params.setMinPrice(minPrice);
        params.setMaxPrice(maxPrice);
        params.setPage(page);
        params.setSize(size);

        return ResponseEntity.ok(bookService.searchBooks(params));
    }

    @Operation(summary = "Obtener facets para filtros dinámicos",
               description = "Devuelve agregaciones: categorías, ratings, rango de precios y disponibilidad de stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facets obtenidos exitosamente")
    })
    @GetMapping("/facets")
    public ResponseEntity<BookFacetsDTO> getFacets() {
        return ResponseEntity.ok(bookService.getFacets());
    }

    @Operation(summary = "Obtener libro por ID", description = "Obtiene los detalles de un libro específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @Parameter(description = "ID del libro", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Crear un libro", description = "Crea un nuevo libro en el catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "El ISBN ya existe")
    })
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookCreateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(dto));
    }

    @Operation(summary = "Actualizar un libro completo (PUT)")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequestDTO dto) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    @Operation(summary = "Actualizar un libro parcialmente (PATCH)")
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> patchBook(
            @PathVariable Long id,
            @Valid @RequestBody BookPatchRequestDTO dto) {
        return ResponseEntity.ok(bookService.patchBook(id, dto));
    }

    @Operation(summary = "Eliminar un libro (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

