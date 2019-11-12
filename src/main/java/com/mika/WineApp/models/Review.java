package com.mika.WineApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Long id;

    private String author;

    private LocalDate date;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(precision = 3, scale = 2)
    private double rating;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "wine_id")
    private Wine wine;

    // Constructors:<
    public Review() {}

    public Review(String author, LocalDate date, String reviewText, double rating, Wine wine) {
        this.author = author;
        this.date = date;
        this.reviewText = reviewText;
        this.rating = rating;
        this.wine = wine;
    }
}