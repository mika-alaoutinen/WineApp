package com.mika.WineApp.models;

import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * Model for wine review.
 */
@Data
@Entity
public class Review {
    private String author;
    private LocalDate date;
    private Wine wine;
    private String reviewText;
    private double rating;

    public Review() {}

    public Review(String author, LocalDate date, Wine wine, String reviewText, double rating) {
        this.author = author;
        this.date = date;
        this.wine = wine;
        this.reviewText = reviewText;
        this.rating = rating;
    }
}