package com.relatosDePapel.relatosservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class BookPatchRequestDTO {

    private String title;

    private String author;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;

    @Min(value = 1, message = "El número de páginas debe ser mayor o igual a 1")
    private Integer pages;

    private String category;

    @Pattern(regexp = "^\\d{3}-\\d-\\d{3}-\\d{5}-\\d$",
            message = "El formato del ISBN no es válido. Debe usar el formato xxx-x-xxx-xxxxx-x con guiones")
    private String isbn;

    @Min(value = 1, message = "El rating mínimo es 1")
    @Max(value = 5, message = "El rating máximo es 5")
    private Integer rating;

    private Boolean visible;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private String description;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
