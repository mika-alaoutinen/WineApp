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

    public WineReviewDto(List<Review> reviews) {
        this.reviews = reviews;
        this.wine = initWine();
    }

    /**
     * Validates that all reviews are about the same wine.
     * If validation is OK, returns Wine from one of the reviews.
     * @return Wine
     */
    private Wine initWine() {
        long numberOfWines = reviews.stream()
                .map(Review::getWine)
                .distinct()
                .count();

        if (numberOfWines != 1) {
            System.out.println("What is this?");
            return new Wine();
        }

        return reviews.get(0).getWine();
    }


}
