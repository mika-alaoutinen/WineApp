package com.mika.WineApp.services;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.errors.InvalidDateException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ReviewService extends CrudService<Review> {

    /**
     * Find all reviews for a given wine.
     *
     * @param wineId to find
     * @return reviews for wine with given id
     */
    List<Review> findByWineId(Long wineId);

    /**
     * Find all reviews for wines that match given name.
     *
     * @param name of wine
     * @return reviews for wine(s) that match given name
     */
    List<Review> findByWineName(String name);

    /**
     * Save new review to repository.
     *
     * @param wineId    to identify what wine the review is about
     * @param newReview to be added
     * @return saved review
     */
    Optional<Review> add(Long wineId, Review newReview);

    /**
     * Checks if the logged in user is allowed to edit or delete a review.
     *
     * @param id of review
     * @return boolean
     * @throws UsernameNotFoundException e
     */
    boolean isAllowedToEdit(Long id) throws UsernameNotFoundException;

    /**
     * Find reviews that match the given criteria. One or many criteria may be given.
     *
     * @param author      of review
     * @param dateRange   to find
     * @param ratingRange to find
     * @return list of reviews matching given criteria
     */
    List<Review> search(String author, List<String> dateRange, List<Double> ratingRange) throws InvalidDateException;

// Quick searches:

    /**
     * Find newest reviews. Default limit is 10.
     *
     * @param limit for returned reviews
     * @return list of found reviews
     */
    List<Review> findNewest(int limit);

    /**
     * Find best rated reviews. Default limit is 10.
     *
     * @param limit for returned reviews
     * @return list of found reviews
     */
    List<Review> findBestRated(int limit);

    /**
     * Find worst rated reviews. Default limit is 10.
     *
     * @param limit for returned reviews
     * @return list of found reviews
     */
    List<Review> findWorstRated(int limit);
}