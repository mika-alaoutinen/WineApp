package com.mika.WineApp.services;

import com.mika.WineApp.errors.forbidden.ForbiddenException;
import com.mika.WineApp.errors.invaliddate.InvalidDateException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.services.impl.ReviewServiceImpl;
import com.mika.WineApp.specifications.ReviewSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ReviewServiceTest extends ServiceTest {
    private static final String author = "Mika";
    private static final Double[] ratingRange = { 1.0, 5.0 };

    @InjectMocks
    private ReviewServiceImpl service;

    @BeforeEach
    void setupTests() {
        this.review = reviews.stream().findAny().orElse(null);
        this.wine = wines.stream().findAny().orElse(null);

        Mockito.lenient()
                .when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.ofNullable(review));

        Mockito.lenient()
                .when(reviewRepository.findById(nonExistingReviewId))
                .thenReturn(Optional.empty());

        Mockito.lenient()
                .when(reviewRepository.save(review))
                .thenReturn(review);
    }

    @Test
    void findAll() {
        var sortedReviews = reviews.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Review::getDate)))
                .collect(Collectors.toList());

        Mockito.when(reviewRepository.findAllByOrderByDateDesc())
               .thenReturn(sortedReviews);

        var foundReviews = service.findAll();

        verify(reviewRepository, times(1)).findAllByOrderByDateDesc();
        assertEquals(sortedReviews, foundReviews);
    }

    @Test
    void findById() {
        System.out.println(review.getId());
        Review foundReview = service.findById(review.getId());

        verify(reviewRepository, times(1)).findById(review.getId());
        assertEquals(review, foundReview);
    }

    @Test
    void findByNonExistingId() {
        Exception e = assertThrows(NotFoundException.class, () -> service.findById(nonExistingReviewId));
        assertEquals(e.getMessage(), "Error: could not find review with id " + nonExistingReviewId);
        verify(reviewRepository, times(1)).findById(nonExistingReviewId);
    }

    @Test
    void findByWineId() {
        Mockito.when(reviewRepository.findByWineId(review.getId()))
               .thenReturn(reviews);

        var foundReviews = service.findByWineId(review.getId());
        assertEquals(2, foundReviews.size());
    }

    @Test
    void findByWineIdReturnsEmptyList() {
        long id = 1L;

        Mockito.when(reviewRepository.findByWineId(id))
               .thenReturn(List.of());

        var foundReviews = service.findByWineId(id);
        assertTrue(foundReviews.isEmpty());
    }

    @Test
    void findByWineName() {
        String wineName = "Valkoviini 1";
        Mockito.when(reviewRepository.findByWineNameContainingIgnoreCase(wineName))
                .thenReturn(reviews);

        var foundReviews = service.findByWineName(wineName);
        assertEquals(2, foundReviews.size());
    }

    @Test
    void addReview() {
        Mockito.when(userService.setUser(review))
               .thenReturn(review);

        Mockito.when(reviewRepository.save(review))
               .thenReturn(review);

        Review savedReview = service.add(wine.getId(), review);

        verify(wineService, times(1)).findById(wine.getId());
        verify(userService, times(1)).setUser(review);
        verify(reviewRepository, times(1)).save(review);
        assertEquals(review, savedReview);
    }

    @Test
    void addReviewForNonExistingWine() {
        Mockito.when(wineService.findById(nonExistingWineId))
               .thenThrow(new NotFoundException(new Wine(), nonExistingWineId));

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.add(nonExistingWineId, review));

        assertEquals(e.getMessage(), "Error: could not find wine with id " + nonExistingWineId);
        verify(wineService, times(1)).findById(nonExistingWineId);
        verify(reviewRepository, times(0)).save(review);
    }

    @Test
    void editReview() {
        Mockito.when(userService.isUserAllowedToEdit(review))
               .thenReturn(true);

        Review editedReview = service.edit(review.getId(), review);
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(userService, times(1)).isUserAllowedToEdit(review);
        verify(reviewRepository, times(1)).save(review);
        assertEquals(review, editedReview);
    }

    @Test
    void editNonExistingReview() {
        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.edit(nonExistingReviewId, review));

        assertEquals(e.getMessage(), "Error: could not find review with id " + nonExistingReviewId);
        verify(reviewRepository, times(1)).findById(nonExistingReviewId);
        verify(reviewRepository, times(0)).save(review);
    }

    @Test
    void editReviewWithoutPermission() {
        ForbiddenException e = assertThrows(ForbiddenException.class, () ->
                service.edit(review.getId(), review));

        assertEquals("Error: tried to modify a review that you do not own!", e.getMessage());
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(userService, times(1)).isUserAllowedToEdit(review);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    void deleteReview() {
        Mockito.when(userService.isUserAllowedToEdit(review))
               .thenReturn(true);

        service.delete(review.getId());
        verify(userService, times(1)).isUserAllowedToEdit(review);
        verify(reviewRepository, times(1)).deleteById(review.getId());
    }

    @Test
    void shouldThrowErrorWhenWrongUserTriesToDelete() {
        Exception e = assertThrows(ForbiddenException.class, () -> service.delete(review.getId()));
        assertEquals("Error: tried to modify a review that you do not own!", e.getMessage());
    }

    @Test
    void count() {
        Mockito.when(reviewRepository.count())
               .thenReturn((long) reviews.size());

        long reviewCount = service.count();

        verify(reviewRepository, times(1)).count();
        assertEquals(reviews.size(), reviewCount);
    }

    @Test
    void isAllowedToEditTrue() {
        isAllowedToEdit(true);
    }

    @Test
    void isAllowedToEditFalse() {
        isAllowedToEdit(false);
    }

    @Test
    void searchWithNullParameters() {
        var result = service.search(null, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void validDateRangeIsParsed() {
        String[] monthRange = { "2020-01", "2020-02" };
        service.search(author, monthRange, ratingRange);
        verify(reviewRepository, times(1)).findAll(any(ReviewSpecification.class));
    }

    @Test
    void dateRangeIsNull() {
        service.search(author, null, ratingRange);
        verify(reviewRepository, times(1)).findAll(any(ReviewSpecification.class));
    }

    @Test
    void invalidDateRangeThrowsException1() {
        String[] monthRange = { "2020-01-01", "2020-02" };
        String expectedErrorMessage = "Error: could not parse date 2020-01-01";
        testInvalidDates(monthRange, expectedErrorMessage);
    }

    @Test
    void invalidDateRangeThrowsException2() {
        String[] monthRange = { "2020-01" };
        String expectedErrorMessage = "Error: date range must have a start date and an end date. Given [2020-01]";
        testInvalidDates(monthRange, expectedErrorMessage);
    }

    private void testInvalidDates(String[] monthRange, String expectedErrorMessage) {
        Exception e = assertThrows(InvalidDateException.class, () ->
                service.search(author, monthRange, ratingRange));

        assertEquals(expectedErrorMessage, e.getMessage());
        verify(reviewRepository, times(0)).findAll(any(ReviewSpecification.class));
    }

    // Private methods:
    private void isAllowedToEdit(boolean isAllowed) {
        Mockito.when(userService.isUserAllowedToEdit(review))
                .thenReturn(isAllowed);

        assertEquals(service.isAllowedToEdit(review.getId()), isAllowed);
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(userService, times(1)).isUserAllowedToEdit(review);
    }
}