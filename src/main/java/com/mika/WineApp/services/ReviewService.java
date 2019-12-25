package com.mika.WineApp.services;

import com.mika.WineApp.models.Review;

import java.util.List;

public interface ReviewService extends CrudService<Review> {
    Review add(Long wineId, Review newReview);

    List<Review> findByAuthor(String author);
    List<Review> findByDate(String startDate, String endDate);
    List<Review> findByRating(double minRating, double maxRating);

    // Quick searches:
    List<Review> findNewest(int limit);
    List<Review> findBestRated(int limit);
    List<Review> findWorstRated(int limit);

    // Find by wine:
    List<Review> findByWineId(Long wineId);
    List<Review> findByWineName(String name);
}