package com.mika.WineApp.services;

import com.mika.WineApp.errors.InvalidDateException;
import com.mika.WineApp.errors.ReviewNotFoundException;
import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.specifications.ReviewSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    private final WineRepository wineRepository;

// --- Find reviews ---
    public List<Review> findAll() {
        return repository.findAll();
    }

    public Optional<Review> findById(Long id) {
        return repository.findById(id);
    }

    public List<Review> findByWineId(Long wineId) {
        return repository.findByWineId(wineId);
    }

    public List<Review> findByWineName(String wineName) {
        return repository.findByWineNameContainingIgnoreCase(wineName);
    }

// --- Add, edit and delete ---
    public Review add(Long wineId, Review newReview) {
        Wine wine = wineRepository
                .findById(wineId)
                .orElseThrow(() -> new WineNotFoundException(wineId));

        newReview.setWine(wine);
        return repository.save(newReview);
    }

    public Review edit(Long id, Review editedReview) {
        Review review = repository
                .findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        // If wine info has been edited, save changes. Don't save null wines.
        if (editedReview.getWine() != null) {
            review.setWine(editedReview.getWine());
        }

        review.setAuthor(editedReview.getAuthor());
        review.setDate(editedReview.getDate());
        review.setReviewText(editedReview.getReviewText());
        review.setRating(editedReview.getRating());

        return repository.save(review);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

// --- Other methods ---
    public long count() {
        return repository.count();
    }

    public List<Review> search(String author, String[] dateRange, Double[] ratingRange) {
        LocalDate[] dates = null;

        if (dateRange != null) {
            dates = Arrays.stream(dateRange)
                    .map(this::parseMonthYear)
                    .toArray(LocalDate[]::new);
        }

        return repository.findAll(new ReviewSpecification(author, dates, ratingRange));
    }

// --- Quick searches ---
    public List<Review> findNewest(int limit) {
        return repository
                .findAllByOrderByDateDesc(PageRequest.of(0, limit))
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
    private LocalDate parseMonthYear(String date) {
        try {
            return YearMonth
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM"))
                    .atDay(1);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }
    }
}