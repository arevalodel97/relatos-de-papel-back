package com.relatosDePapel.relatosservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class BookSearchParamsDTO {

    private String title;
    private String author;

    @JsonProperty("publication_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    private String category;
    private String isbn;
    private Integer rating;
    private Boolean visible;

    /** Filtro de stock: true = solo con stock > 0, false = solo sin stock, null = todos */
    private Boolean inStock;

    /** Rango de precios */
    private Float minPrice;
    private Float maxPrice;

    /** Paginación */
    private int page = 0;
    private int size = 10;

    // Constructors
    public BookSearchParamsDTO() {
    }

    public BookSearchParamsDTO(String title, String author, LocalDate publicationDate,
                              String category, String isbn, Integer rating, Boolean visible,
                              Boolean inStock, Float minPrice, Float maxPrice) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.category = category;
        this.isbn = isbn;
        this.rating = rating;
        this.visible = visible;
        this.inStock = inStock;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
