package com.mika.WineApp.models;

import lombok.Data;

import javax.persistence.Entity;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Author {
    private String name;
    private List<Review> reviews;
    private Set<Wine> top5Wines; // highest rated wines
}
