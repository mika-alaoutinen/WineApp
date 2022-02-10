package com.mika.WineApp.models.review;

import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Review implements EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    private Wine wine;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Review(String author, LocalDate date, String reviewText, double rating, Wine wine) {
        this.author = author;
        this.date = date;
        this.reviewText = reviewText;
        this.rating = rating;
        this.wine = wine;
    }
}