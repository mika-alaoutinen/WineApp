package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.forbidden.ForbiddenException;
import com.mika.WineApp.errors.invaliddate.InvalidDateException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.services.ReviewService;
import com.mika.WineApp.services.UserService;
import com.mika.WineApp.services.WineService;
import com.mika.WineApp.specifications.ReviewSpecification;
import com.mika.WineApp.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    private final UserService userService;
    private final WineService wineService;

    // --- Find reviews ---
    public List<Review> findAll() {
        return repository.findAllByOrderByDateDesc();
    }

    public Review findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(new Review(), id));
    }

    public List<Review> findByWineId(Long wineId) {
        return repository.findByWineId(wineId);
    }

    public List<Review> findByWineName(String wineName) {
        return repository.findByWineNameContainingIgnoreCase(wineName);
    }

    // --- Add, edit and delete ---
    public Review add(Long wineId, Review newReview) {
        Review review = (Review) userService.setUser(newReview);
        Wine wine = wineService.findById(wineId);
        review.setWine(wine);
        return repository.save(review);
    }

    public Review edit(Long id, Review editedReview) {
        Review review = findAndValidateReview(id);

        review.setAuthor(editedReview.getAuthor());
        review.setDate(editedReview.getDate());
        review.setReviewText(editedReview.getReviewText());
        review.setRating(editedReview.getRating());

        return repository.save(review);
    }

    public void delete(Long id) {
        findAndValidateReview(id);
        repository.deleteById(id);
    }

    // --- Other methods ---
    public int count() {
        return (int) repository.count();
    }

    public boolean isAllowedToEdit(Long id) {
        Review review = findById(id);
        return userService.isUserAllowedToEdit(review);
    }

    public List<Review> search(String author, String[] dateRange, Double[] ratingRange)
            throws InvalidDateException {

        if (author == null && dateRange == null && ratingRange == null) {
            return Collections.emptyList();
        }

        LocalDate[] dates = DateUtils.parseMonthRange(dateRange);
        return repository.findAll(new ReviewSpecification(author, dates, ratingRange));
    }

    // --- Quick searches ---
    public List<Review> findNewest(int limit) {
        return repository
                .findAllDistinctByOrderByDateDesc(PageRequest.of(0, limit))
                .getContent();
    }

    public List<Review> findBestRated(int limit) {
        return repository
                .findAllByOrderByRatingDesc(PageRequest.of(0, limit))
                .getContent();
    }

    public List<Review> findWorstRated(int limit) {
        return repository
                .findAllByOrderByRatingAsc(PageRequest.of(0, limit))
                .getContent();
    }

    // --- Utility methods ---
    private Review findAndValidateReview(Long id) {
        Review review = findById(id);

        if (!userService.isUserAllowedToEdit(review)) {
            throw new ForbiddenException(review);
        }

        return review;
    }
}