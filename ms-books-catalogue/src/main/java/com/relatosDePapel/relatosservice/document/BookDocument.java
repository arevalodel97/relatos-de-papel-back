package com.relatosDePapel.relatosservice.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Documento que representa un libro en el índice OpenSearch "books"
 * Los nombres de campo deben coincidir exactamente con el mapping de OpenSearch
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDocument {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer pages;
    private String description;
    private Integer rating;
    private String category;
    private Integer stock;
    private Boolean visible;

    @JsonProperty("publication_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    private Float price;
    private String photo;

    public BookDocument() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
}
