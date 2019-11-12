package com.mika.WineApp.dtos;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;

import java.util.List;

/**
 * A DTO that packages all information regarding a wine review.
 */
public class WineReviewDto {
    private Wine wine;
    private List<Review> reviews;

    public WineReviewDto(Wine wine, List<Review> reviews) {
        this.wine = wine;
        this.reviews = reviews;
    }
}
