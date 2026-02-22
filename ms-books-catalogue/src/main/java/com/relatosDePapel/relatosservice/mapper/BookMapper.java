package com.relatosDePapel.relatosservice.mapper;

import com.relatosDePapel.relatosservice.document.BookDocument;
import com.relatosDePapel.relatosservice.dto.*;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDocument toDocument(BookCreateRequestDTO dto, Long id) {
        BookDocument doc = new BookDocument();
        doc.setId(id);
        doc.setTitle(dto.getTitle());
        doc.setAuthor(dto.getAuthor());
        doc.setPublicationDate(dto.getPublicationDate());
        doc.setPages(dto.getPages() != null ? dto.getPages() : 1);
        doc.setCategory(dto.getCategory());
        doc.setIsbn(dto.getIsbn());
        doc.setRating(dto.getRating());
        doc.setVisible(dto.getVisible() != null ? dto.getVisible() : Boolean.TRUE);
        doc.setStock(dto.getStock() != null ? dto.getStock() : 0);
        doc.setDescription(dto.getDescription());
        doc.setPrice(dto.getPrice());
        doc.setPhoto(dto.getPhoto());
        return doc;
    }

    public BookDocument toDocument(BookUpdateRequestDTO dto, Long id) {
        BookDocument doc = new BookDocument();
        doc.setId(id);
        doc.setTitle(dto.getTitle());
        doc.setAuthor(dto.getAuthor());
        doc.setPublicationDate(dto.getPublicationDate());
        doc.setPages(dto.getPages());
        doc.setCategory(dto.getCategory());
        doc.setIsbn(dto.getIsbn());
        doc.setRating(dto.getRating());
        doc.setVisible(dto.getVisible());
        doc.setStock(dto.getStock());
        doc.setDescription(dto.getDescription());
        doc.setPrice(dto.getPrice());
        doc.setPhoto(dto.getPhoto());
        return doc;
    }

    public void updateFromPatchDTO(BookDocument doc, BookPatchRequestDTO dto) {
        if (dto.getTitle() != null) doc.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) doc.setAuthor(dto.getAuthor());
        if (dto.getPublicationDate() != null) doc.setPublicationDate(dto.getPublicationDate());
        if (dto.getPages() != null) doc.setPages(dto.getPages());
        if (dto.getCategory() != null) doc.setCategory(dto.getCategory());
        if (dto.getIsbn() != null) doc.setIsbn(dto.getIsbn());
        if (dto.getRating() != null) doc.setRating(dto.getRating());
        if (dto.getVisible() != null) doc.setVisible(dto.getVisible());
        if (dto.getStock() != null) doc.setStock(dto.getStock());
        if (dto.getDescription() != null) doc.setDescription(dto.getDescription());
        if (dto.getPrice() != null) doc.setPrice(dto.getPrice());
        if (dto.getPhoto() != null) doc.setPhoto(dto.getPhoto());
    }

    public BookResponseDTO toResponseDTO(BookDocument doc) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(doc.getId());
        dto.setTitle(doc.getTitle());
        dto.setAuthor(doc.getAuthor());
        dto.setPublicationDate(doc.getPublicationDate());
        dto.setPages(doc.getPages());
        dto.setCategory(doc.getCategory());
        dto.setIsbn(doc.getIsbn());
        dto.setRating(doc.getRating());
        dto.setVisible(doc.getVisible());
        dto.setStock(doc.getStock());
        dto.setDescription(doc.getDescription());
        dto.setPrice(doc.getPrice());
        dto.setPhoto(doc.getPhoto());
        return dto;
    }
}
