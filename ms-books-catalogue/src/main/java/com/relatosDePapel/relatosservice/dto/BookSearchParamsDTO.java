package com.relatosDePapel.relatosservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class BookSearchParamsDTO {

    private String title;
    private String author;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;

    private String category;
    private String isbn;
    private Integer rating;
    private Boolean visible;

    // Constructors
    public BookSearchParamsDTO() {
    }

    public BookSearchParamsDTO(String title, String author, LocalDate publishedDate,
                              String category, String isbn, Integer rating, Boolean visible) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.category = category;
        this.isbn = isbn;
        this.rating = rating;
        this.visible = visible;
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

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
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
}
