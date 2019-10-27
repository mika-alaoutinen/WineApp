package com.mika.WineApp.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/** Model for wine review. */
@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "rating")
    private double rating;

    // one Review can have on Wine. One wine can have many Reviews.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId
//    @JoinColumn(name = "wine_id", referencedColumnName = "id")
    private Wine wine;

    public Review() {}

    public Review(String author, LocalDate date, String reviewText, double rating, Wine wine) {
        this.author = author;
        this.date = date;
        this.reviewText = reviewText;
        this.rating = rating;
        this.wine = wine;
    }
}