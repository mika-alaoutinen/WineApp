package com.mika.WineApp.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
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
    @ToString.Exclude
    private Wine wine;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Review review = (Review) o;
        return id != null && Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}