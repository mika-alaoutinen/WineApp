package com.mika.WineApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private WineType type;

    private String country;

    @Column(precision = 10, scale = 2)
    private double price;

    @Column(precision = 10, scale = 2)
    private double quantity;

    @ElementCollection
    @CollectionTable(name = "wine_descriptions", joinColumns = @JoinColumn(name = "id"))
    private List<String> description;

    @ElementCollection
    @CollectionTable(name = "wine_food_pairings", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "food_pairings")
    private List<String> foodPairings;

    private String url;

    @JsonIgnore
    @OneToMany(mappedBy = "wine", cascade = CascadeType.ALL)
    private List<Review> reviews;

    // Constructors:
    public Wine() {
        this.reviews = new ArrayList<>();
    }

    public Wine(String name,
                WineType type,
                String country,
                double price,
                double quantity,
                List<String> description,
                List<String>foodPairings,
                String url) {

        this.name = name;
        this.type = type;
        this.country = country;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.foodPairings = foodPairings;
        this.url = url;
        this.reviews = new ArrayList<>();
    }

    public void addReview(Review review) {
        if (!reviews.contains(review)) {
            reviews.add(review);
        }
    }
}