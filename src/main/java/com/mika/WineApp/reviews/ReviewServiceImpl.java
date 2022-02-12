package com.mika.WineApp.reviews;

import com.mika.WineApp.errors.ForbiddenException;
import com.mika.WineApp.errors.InvalidDateException;
import com.mika.WineApp.reviews.model.Review;
import com.mika.WineApp.services.ReviewService;
import com.mika.WineApp.services.UserService;
import com.mika.WineApp.services.WineService;
import com.mika.WineApp.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    private final UserService userService;
    private final WineService wineService;

    // --- Find reviews ---
    @Override
    public List<Review> findAll() {
        return repository.findAllByOrderByDateDesc();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Review> findByWineId(Long wineId) {
        return repository.findByWineId(wineId);
    }

    @Override
    public List<Review> findByWineName(String wineName) {
        return repository.findByWineNameContainingIgnoreCase(wineName);
    }

    // --- Add, edit and delete ---
    @Override
    public Optional<Review> add(Long wineId, Review newReview) {
        Review review = (Review) userService.setUser(newReview);
        return wineService
                .findById(wineId)
                .map(wine -> {
                    review.setWine(wine);
                    return review;
                })
                .map(repository::save);
    }

    @Override
    public Optional<Review> edit(Long id, Review editedReview) {
        Optional<Review> reviewOpt = findById(id);
        if (reviewOpt.isEmpty()) {
            return Optional.empty();
        }

        Review review = reviewOpt.get();
        validateEditPermission(review);

        review.setAuthor(editedReview.getAuthor());
        review.setDate(editedReview.getDate());
        review.setReviewText(editedReview.getReviewText());
        review.setRating(editedReview.getRating());

        return Optional.of(repository.save(review));
    }

    @Override
    public void delete(Long id) {
        findById(id).ifPresent(this::validateEditPermission);
        repository.deleteById(id);
    }

    // --- Other methods ---
    @Override
    public int count() {
        return (int) repository.count();
    }

    @Override
    public boolean isAllowedToEdit(Long id) {
        return findById(id)
                .map(userService::isUserAllowedToEdit)
                .orElse(false);
    }

    @Override
    public List<Review> search(String author, List<String> dateRange, List<Double> ratingRange)
            throws InvalidDateException {

        if (author == null && dateRange == null && ratingRange == null) {
            return Collections.emptyList();
        }

        List<LocalDate> dates = DateUtils.parseMonthRange(dateRange);
        return repository.findAll(new ReviewSpecification(author, dates, ratingRange));
    }

    // --- Quick searches ---
    @Override
    public List<Review> findNewest(int limit) {
        return repository
                .findAllDistinctByOrderByDateDesc(PageRequest.of(0, limit))
                .getContent();
    }

    @Override
    public List<Review> findBestRated(int limit) {
        return repository
                .findAllByOrderByRatingDesc(PageRequest.of(0, limit))
                .getContent();
    }

    @Override
    public List<Review> findWorstRated(int limit) {
        return repository
                .findAllByOrderByRatingAsc(PageRequest.of(0, limit))
                .getContent();
    }

    private void validateEditPermission(Review review) {
        if (!userService.isUserAllowedToEdit(review)) {
            throw new ForbiddenException(review);
        }
    }
}