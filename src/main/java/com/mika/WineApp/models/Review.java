package com.mika.WineApp.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Review extends EntityModel {

    @NotBlank
    private String author;

    @NotNull
    @PastOrPresent
    private LocalDate date;

    @NotBlank
    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Min(value = 0)
    @Max(value = 5)
    @NotNull
    @Column(precision = 3, scale = 2)
    private double rating;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "wine_id")
    private Wine wine;

    // Constructors:
    public Review() {
        super();
    }

    public Review(String author, LocalDate date, String reviewText, double rating, Wine wine) {
        super();
        this.author = author;
        this.date = date;
        this.reviewText = reviewText;
        this.rating = rating;
        this.wine = wine;
    }
}