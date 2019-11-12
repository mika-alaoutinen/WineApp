package com.mika.WineApp.dtos;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;

import java.util.List;

/**
 * A DTO that packages all information regarding a wine review.
 */
public class WineReviewDto {
    private final List<Review> reviews;
    private final Wine wine;

    public WineReviewDto(List<Review> reviews, Wine wine) {
        this.reviews = reviews;
        this.wine = wine;
    }
}
