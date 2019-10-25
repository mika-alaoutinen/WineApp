package com.mika.WineApp.models;

import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

/**
 * Model for wine.
 */
@Data
@Entity
public class Wine {
    private final String name;
    private final WineType type;
    private final String country;
    private final double price;
    private final double quantity;
    private final List<String> description;
    private final List<String> foodPairings;
    private String url;

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