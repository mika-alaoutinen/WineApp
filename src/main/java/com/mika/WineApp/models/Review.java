package com.mika.WineApp.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(name = "rating")
    private double rating; // add precision

    @ManyToOne(fetch = FetchType.LAZY)
    private Wine wine; //    @JoinColumn(name = "wine_id")

    public Review() {}

    public Review(String author, LocalDate date, String reviewText, double rating, Wine wine) {
        this.author = author;
        this.date = date;
        this.reviewText = reviewText;
        this.rating = rating;
        this.wine = wine;
    }
}