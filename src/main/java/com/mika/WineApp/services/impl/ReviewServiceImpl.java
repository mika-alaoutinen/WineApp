package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.invaliddate.InvalidDateException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.security.SecurityUtilities;
import com.mika.WineApp.services.ReviewService;
import com.mika.WineApp.services.UserService;
import com.mika.WineApp.services.WineService;
import com.mika.WineApp.specifications.ReviewSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    private final SecurityUtilities securityUtils;
    private final UserService userService;
    private final WineService wineService;

// --- Find reviews ---
    public List<Review> findAll() {
        return repository.findAllByOrderByDateDesc();
    }

    public Review findById(Long id) {
        return repository.findById(id)
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
        Wine wine = wineService.findById(wineId);
        newReview.setWine(wine);

        String username = securityUtils.getUsernameFromSecurityContext();
        User user = userService.findByUserName(username);
        newReview.setUser(user);

        return repository.save(newReview);
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
    public long count() {
        return repository.count();
    }

    public List<Review> search(String author, String[] dateRange, Double[] ratingRange) {
        if (author == null && dateRange == null && ratingRange == null) {
            return  new ArrayList<>();
        }

        LocalDate[] dates = parseMonthRange(dateRange);
        return repository.findAll(new ReviewSpecification(author, dates, ratingRange));
    }

    public boolean isAllowedToEdit(Long id) {
        Review review = findById(id);
        String owner = review.getUser().getUsername();
        String user = securityUtils.getUsernameFromSecurityContext();
        return owner.equals(user);
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
        User user = userService.findByUserName(securityUtils.getUsernameFromSecurityContext());
        securityUtils.validateUpdateRequest(review, user);
        return review;
    }

    private LocalDate[] parseMonthRange(String[] dates) throws InvalidDateException {
        if (dates == null) {
            return null;
        }

        if (dates.length != 2) {
            throw new InvalidDateException(dates);
        }

        LocalDate[] parsedDates = new LocalDate[2];
        String date = dates[0];

        try {
            parsedDates[0] = YearMonth
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM"))
                    .atDay(1);

            date = dates[1];

            parsedDates[1] = YearMonth
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM"))
                    .atEndOfMonth();

        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }

        return parsedDates;
    }
}