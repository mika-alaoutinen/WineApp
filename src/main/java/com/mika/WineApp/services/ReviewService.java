package com.mika.WineApp.services;

import com.mika.WineApp.models.Review;

import java.util.List;

public interface ReviewService extends CrudService<Review> {
    // Find by wine:
    List<Review> findByWineId(Long wineId);
    List<Review> findByWineName(String name);

    Review add(Long wineId, Review newReview);
    List<Review> search(String author, String[] dateRange, Double[] ratingRange);

    // Quick searches:
    List<Review> findNewest(int limit);
    List<Review> findBestRated(int limit);
    List<Review> findWorstRated(int limit);
}