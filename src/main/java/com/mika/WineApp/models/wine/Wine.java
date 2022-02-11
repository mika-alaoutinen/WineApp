package com.mika.WineApp.models.wine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Wine implements EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

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
    private List<@NotBlank String> description = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "wine_food_pairings", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "food_pairings")
    private List<@NotBlank String> foodPairings = new ArrayList<>();

    private String url;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wine")
    private List<@NotNull Review> reviews = new ArrayList<>();
}