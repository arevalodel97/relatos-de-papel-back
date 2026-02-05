package com.relatosDePapel.relatosservice.mapper;

import com.relatosDePapel.relatosservice.dto.*;
import com.relatosDePapel.relatosservice.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookCreateRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublishedDate(dto.getPublishedDate());
        book.setPages(dto.getPages() != null ? dto.getPages() : Integer.valueOf(1));
        book.setCategory(dto.getCategory());
        // No formatear ISBN aquí: la validación del DTO exige el formato con guiones
        book.setIsbn(dto.getIsbn());
        book.setRating(dto.getRating());
        book.setVisible(dto.getVisible() != null ? dto.getVisible() : Boolean.TRUE);
        book.setStock(dto.getStock() != null ? dto.getStock() : Integer.valueOf(0));
        book.setDescription(dto.getDescription());
        return book;
    }

    public Book toEntity(BookUpdateRequestDTO dto, Long id) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublishedDate(dto.getPublishedDate());
        book.setPages(dto.getPages());
        book.setCategory(dto.getCategory());
        book.setIsbn(dto.getIsbn());
        book.setRating(dto.getRating());
        book.setVisible(dto.getVisible());
        book.setStock(dto.getStock());
        book.setDescription(dto.getDescription());
        return book;
    }

    public void updateFromPatchDTO(Book book, BookPatchRequestDTO dto) {
        if (dto.getTitle() != null) {
            book.setTitle(dto.getTitle());
        }
        if (dto.getAuthor() != null) {
            book.setAuthor(dto.getAuthor());
        }
        if (dto.getPublishedDate() != null) {
            book.setPublishedDate(dto.getPublishedDate());
        }
        if (dto.getPages() != null) {
            book.setPages(dto.getPages());
        }
        if (dto.getCategory() != null) {
            book.setCategory(dto.getCategory());
        }
        if (dto.getIsbn() != null) {
            // Mantener tal cual: el DTO ya valida el formato con guiones
            book.setIsbn(dto.getIsbn());
        }
        if (dto.getRating() != null) {
            book.setRating(dto.getRating());
        }
        if (dto.getVisible() != null) {
            book.setVisible(dto.getVisible());
        }
        if (dto.getStock() != null) {
            book.setStock(dto.getStock());
        }
        if (dto.getDescription() != null) {
            book.setDescription(dto.getDescription());
        }
    }

    public BookResponseDTO toResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setPages(book.getPages());
        dto.setCategory(book.getCategory());
        dto.setIsbn(book.getIsbn());
        dto.setRating(book.getRating());
        dto.setVisible(book.getVisible());
        dto.setStock(book.getStock());
        dto.setDescription(book.getDescription());
        return dto;
    }
}
