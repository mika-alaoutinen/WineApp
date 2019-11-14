package com.mika.WineApp.services;

import com.mika.WineApp.errors.InvalidDateException;
import com.mika.WineApp.errors.ReviewNotFoundException;
import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class ReviewService {
    private final ReviewRepository repository;
    private final WineRepository wineRepository;

    public ReviewService(ReviewRepository repository, WineRepository wineRepository) {
        this.repository = repository;
        this.wineRepository = wineRepository;
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    public List<Review> findByAuthor(String author) {
        return repository.findByAuthorIgnoreCase(author);
    }

    public List<Review> findByDate(String startDate, String endDate) {
        LocalDate start = parseDateString(startDate);
        LocalDate end;

        // If no end date is given with request, use current date:
        if (endDate.equals("today")) {
            end = LocalDate.now();
        } else {
            end = parseDateString(endDate);
        }

        return repository.findDistinctByDateBetweenOrderByDateDesc(start, end);
    }

    public List<Review> findByRating(double minRating, double maxRating) {
        return repository.findDistinctByRatingBetweenOrderByRatingDesc(minRating, maxRating);
    }

    public Optional<Review> find(Long id) {
        return repository.findById(id);
    }

// Add, edit and delete:
    public Review add(Review newReview) {
        return repository.save(newReview);
    }

    public Review edit(Review editedReview, Long id) {
        return repository.findById(id).map(review -> {
            review.setAuthor(editedReview.getAuthor());
            review.setDate(editedReview.getDate());
            review.setReviewText(editedReview.getReviewText());
            review.setRating(editedReview.getRating());
            review.setWine(editedReview.getWine());
            return repository.save(review);
        }).orElseThrow(() -> new ReviewNotFoundException(id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

// CRUD operations based on wines:
    public List<Review> findByWineId(Long wineId) {
        return repository.findByWineId(wineId);
    }

    public List<Review> findByWineName(String wineName) {
        return repository.findByWineNameContainingIgnoreCase(wineName);
    }

    public Review add(Long wineId, Review newReview) {
        // TODO: Does this work?
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new WineNotFoundException(wineId));

        wine.addReview(newReview);
        wineRepository.save(wine);

        return newReview;
    }

    // edit and delete

// Utility methods
    private LocalDate parseDateString(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(dateString);
        }
    }
}