package com.mika.WineApp.dtos;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import lombok.Data;

import java.util.List;

/**
 * A DTO that packages a wine together with all it's reviews.
 */
@Data
public class WineReviewDto {
    private final Wine wine;
    private final List<Review> reviews;

    public WineReviewDto(Wine wine, List<Review> reviews) {
        this.wine = wine;
        this.reviews = reviews;
    }
}
