package com.mika.WineApp.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "wines")
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10)
    private WineType type;

    @Column(name = "country")
    private String country;

    @Column(name = "price")
    private double price; // add precision

    @Column(name = "quantity")
    private double quantity; // add precision

    @ElementCollection
    @CollectionTable(name = "wine_descriptions", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "description")
    private List<String> description;

    @ElementCollection
    @CollectionTable(name = "wine_food_pairings", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "food_pairings")
    private List<String> foodPairings;

    @Column(name = "url")
    private String url;

    public Wine() {}

    public Wine(String name, WineType type, String country, double price, double quantity, List<String> description, List<String>foodPairings, String url) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.foodPairings = foodPairings;
        this.url = url;
    }
}