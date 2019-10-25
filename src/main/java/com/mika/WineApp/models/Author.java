package com.mika.WineApp.models;

import lombok.Data;

import javax.persistence.Entity;
import java.util.List;
import java.util.Set;

/*
TODO:
  is this model really needed?

  You can find a list of reviews by a specific
  author from querying ReviewRepository. At least Author shouldn't be persisted
  in a database, because the information is redundant!

  Maybe generate an author in memory when reviews are queried by author?
*/

@Data
public class Author {
    private String name;
    private List<Review> reviews;
    private Set<Wine> top5Wines; // highest rated wines
}
