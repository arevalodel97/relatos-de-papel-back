package com.relatosDePapel.relatosservice.service;

import com.relatosDePapel.relatosservice.dto.*;
import com.relatosDePapel.relatosservice.entity.Book;
import com.relatosDePapel.relatosservice.exception.BookNotFoundException;
import com.relatosDePapel.relatosservice.exception.DuplicateIsbnException;
import com.relatosDePapel.relatosservice.mapper.BookMapper;
import com.relatosDePapel.relatosservice.repository.BookRepository;
import com.relatosDePapel.relatosservice.specification.BookSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de libros
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Listar todos los libros (solo visibles por defecto)
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        BookSearchParamsDTO defaultParams = new BookSearchParamsDTO();
        defaultParams.setVisible(true); // Solo visibles por defecto

        Specification<Book> spec = BookSpecification.withFilters(defaultParams);
        return bookRepository.findAll(spec).stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar libro por ID
     */
    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toResponseDTO(book);
    }

    /**
     * Crear un nuevo libro
     */
    @Override
    @Transactional
    public BookResponseDTO createBook(BookCreateRequestDTO dto) {
        // Verificar que el ISBN no existe
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateIsbnException(dto.getIsbn());
        }

        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponseDTO(savedBook);
    }

    /**
     * Actualizar un libro completo (PUT)
     */
    @Override
    @Transactional
    public BookResponseDTO updateBook(Long id, BookUpdateRequestDTO dto) {
        // Verificar que el libro existe
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }

        // Verificar que el ISBN no esté duplicado (excepto el mismo libro)
        if (bookRepository.existsByIsbnAndIdNot(dto.getIsbn(), id)) {
            throw new DuplicateIsbnException(dto.getIsbn());
        }

        Book book = bookMapper.toEntity(dto, id);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponseDTO(savedBook);
    }

    /**
     * Actualizar un libro parcialmente (PATCH)
     */
    @Override
    @Transactional
    public BookResponseDTO patchBook(Long id, BookPatchRequestDTO dto) {
        // 1. Traer la entidad actual o lanzar 404
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // 2. Si el patch incluye ISBN y es distinto al actual, validar duplicidad
        if (dto.getIsbn() != null && !dto.getIsbn().equals(book.getIsbn())) {
            if (bookRepository.existsByIsbn(dto.getIsbn())) {
                throw new DuplicateIsbnException(dto.getIsbn());
            }
        }

        // 3. Aplicar cambios parciales (SIEMPRE, no solo si cambia ISBN)
        bookMapper.updateFromPatchDTO(book, dto);

        // 4. Guardar y retornar
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponseDTO(savedBook);
    }

    /**
     * Eliminar un libro (soft delete - cambia visible a false)
     */
    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Soft delete: cambiar visible a false en lugar de eliminar
        book.setVisible(false);
        bookRepository.save(book);
    }

    /**
     * Buscar libros con filtros combinados (0..N parámetros)
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchBooks(BookSearchParamsDTO params) {
        Specification<Book> spec = BookSpecification.withFilters(params);
        return bookRepository.findAll(spec).stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
