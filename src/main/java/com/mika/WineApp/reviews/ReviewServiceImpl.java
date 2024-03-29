package com.mika.WineApp.reviews;

import com.mika.WineApp.authentication.UserAuthentication;
import com.mika.WineApp.entities.Review;
import com.mika.WineApp.errors.ForbiddenException;
import com.mika.WineApp.errors.InvalidDateException;
import com.mika.WineApp.search.ReviewSearchParams;
import com.mika.WineApp.search.ReviewSpecification;
import com.mika.WineApp.services.ReviewService;
import com.mika.WineApp.services.WineService;
import com.mika.WineApp.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    private final UserAuthentication userAuth;
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
        Review review = (Review) userAuth.setUser(newReview);
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
                .map(userAuth::isUserAllowedToEdit)
                .orElse(false);
    }

    @Override
    public List<Review> search(String author, List<String> dateRange, List<Double> ratingRange)
            throws InvalidDateException {

        var searchParams = ReviewSearchParams
                .builder()
                .author(author)
                .dateRange(DateUtils.parseMonthRange(dateRange))
                .ratingRange(ratingRange)
                .build();

        return searchParams.isEmpty()
               ? Collections.emptyList()
               : repository.findAll(ReviewSpecification.of(searchParams));
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
        if (!userAuth.isUserAllowedToEdit(review)) {
            throw new ForbiddenException("review");
        }
    }
}