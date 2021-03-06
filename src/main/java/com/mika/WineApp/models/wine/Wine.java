package com.mika.WineApp.models.wine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.review.Review;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Wine extends EntityModel {

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private WineType type;

    @NotBlank
    private String country;

    @PositiveOrZero
    @Column(precision = 10, scale = 2)
    private double price;

    @Positive
    @Column(precision = 10, scale = 2)
    private double volume;

    @ElementCollection
    @CollectionTable(name = "wine_descriptions", joinColumns = @JoinColumn(name = "id"))
    private List<@NotBlank String> description;

    @ElementCollection
    @CollectionTable(name = "wine_food_pairings", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "food_pairings")
    private List<@NotBlank String> foodPairings;

    private String url;

    @JsonIgnore
    @OneToMany(mappedBy = "wine", cascade = CascadeType.ALL)
    private List<@NotNull Review> reviews = List.of();

    public Wine(String name,
                WineType type,
                String country,
                double price,
                double volume,
                List<String> description,
                List<String>foodPairings,
                String url) {

        this.name = name;
        this.type = type;
        this.country = country;
        this.price = price;
        this.volume = volume;
        this.description = description;
        this.foodPairings = foodPairings;
        this.url = url;
        this.reviews = List.of();
    }
}