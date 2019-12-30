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

// --- CRUD service methods ---
    public List<Review> findAll() {
        return repository.findAll();
    }

    public Optional<Review> findById(Long id) {
        return repository.findById(id);
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

    public long count() {
        return repository.count();
    }

// --- Review service methods ---
    public Review add(Long wineId, Review newReview) {
        Wine wine = wineRepository
                .findById(wineId)
                .orElseThrow(() -> new WineNotFoundException(wineId));

        newReview.setWine(wine);
        return repository.save(newReview);
    }

    public List<Review> findByAuthor(String author) {
        return repository.findDistinctByAuthorIgnoreCase(author);
    }

    public List<Review> findByDate(String startDate, String endDate) {
        LocalDate start = parseDate(startDate);
        LocalDate end;

        // If no end date parameter is given, use current date:
        if (endDate.equals("today")) {
            end = LocalDate.now();
        } else {
            end = parseDate(endDate);
        }

        return repository.findDistinctByDateBetweenOrderByDateDesc(start, end);
    }

    public List<Review> findByRating(double minRating, double maxRating) {
        return repository.findDistinctByRatingBetweenOrderByRatingDesc(minRating, maxRating);
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

// --- Search ---
    public List<Review> search(String author, String[] dateRange, Double[] ratingRange) {
        LocalDate[] dates = null;

        if (dateRange != null) {
            dates = Arrays.stream(dateRange)
                    .map(this::parseMonthYear)
                    .toArray(LocalDate[]::new);
        }

        Review review = new Review(author, null, null, null, null);
        return repository.findAll(new ReviewSpecification(review, dates, ratingRange));
    }

// --- Find  operations based on wines ---
    public List<Review> findByWineId(Long wineId) {
        return repository.findByWineId(wineId);
    }

    public List<Review> findByWineName(String wineName) {
        return repository.findByWineNameContainingIgnoreCase(wineName);
    }

// --- Utility methods ---
    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }
    }

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