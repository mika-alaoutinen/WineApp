package com.mika.WineApp.services;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.repositories.ReviewRepository;

import java.util.List;
import java.util.Optional;

public class ReviewService {
    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    public List<Review> findByWineId(Long wineId) {
        return repository.findByWineId(wineId);
    }

    public List<Review> findAllByWineName(String wineName) {
        return repository.findByWineNameContainingIgnoreCase(wineName);
    }

    public Optional<Review> find(Long id) {
        return repository.findById(id);
    }

    public Review add(Review newReview) {
        return repository.save(newReview);
    }

    public Review edit(Review editedReview, Long id) {
        repository.findById(id).ifPresent(review -> {
            review.setAuthor(editedReview.getAuthor());
            review.setDate(editedReview.getDate());
            review.setReviewText(editedReview.getReviewText());
            review.setRating(editedReview.getRating());
            review.setWine(editedReview.getWine());
        });

        return editedReview;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
