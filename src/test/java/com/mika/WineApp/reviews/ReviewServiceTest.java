package com.mika.WineApp.reviews;

import com.mika.WineApp.errors.ForbiddenException;
import com.mika.WineApp.errors.InvalidDateException;
import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.reviews.model.Review;
import com.mika.WineApp.services.ServiceTest;
import com.mika.WineApp.wines.model.Wine;
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
import static org.mockito.Mockito.*;

class ReviewServiceTest extends ServiceTest {
    private static final String author = "Mika";
    private static final List<Double> ratingRange = List.of(1.0, 5.0);

    @InjectMocks
    private ReviewServiceImpl service;

    @BeforeEach
    void setupTests() {
        this.review = reviews
                .stream()
                .findAny()
                .orElse(null);

        this.wine = wines
                .stream()
                .findAny()
                .orElse(null);

        Mockito
                .lenient()
                .when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.ofNullable(review));

        Mockito
                .lenient()
                .when(reviewRepository.findById(nonExistingReviewId))
                .thenReturn(Optional.empty());

        Mockito
                .lenient()
                .when(reviewRepository.save(review))
                .thenReturn(review);
    }

    @Test
    void findAll() {
        var sortedReviews = reviews
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Review::getDate)))
                .collect(Collectors.toList());

        when(reviewRepository.findAllByOrderByDateDesc()).thenReturn(sortedReviews);
        var foundReviews = service.findAll();

        verify(reviewRepository, times(1)).findAllByOrderByDateDesc();
        assertEquals(sortedReviews, foundReviews);
    }

    @Test
    void findById() {
        Review foundReview = service
                .findById(review.getId())
                .get();
        verify(reviewRepository, times(1)).findById(review.getId());
        assertEquals(review, foundReview);
    }

    @Test
    void findByNonExistingId() {
        assertTrue(service
                .findById(nonExistingReviewId)
                .isEmpty());
        verify(reviewRepository, times(1)).findById(nonExistingReviewId);
    }

    @Test
    void findByWineId() {
        when(reviewRepository.findByWineId(review.getId())).thenReturn(reviews);
        var foundReviews = service.findByWineId(review.getId());
        assertEquals(2, foundReviews.size());
    }

    @Test
    void findByWineIdReturnsEmptyList() {
        long id = 1L;
        when(reviewRepository.findByWineId(id)).thenReturn(List.of());

        var foundReviews = service.findByWineId(id);
        assertTrue(foundReviews.isEmpty());
    }

    @Test
    void findByWineName() {
        String wineName = "Valkoviini 1";
        when(reviewRepository.findByWineNameContainingIgnoreCase(wineName)).thenReturn(reviews);

        var foundReviews = service.findByWineName(wineName);
        assertEquals(2, foundReviews.size());
    }

    @Test
    void addReview() {
        when(userService.setUser(any(Review.class))).thenReturn(review);
        when(wineService.findById(wine.getId())).thenReturn(Optional.of(wine));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review savedReview = service
                .add(wine.getId(), review)
                .get();

        verify(wineService, times(1)).findById(wine.getId());
        verify(userService, times(1)).setUser(review);
        verify(reviewRepository, times(1)).save(review);
        assertEquals(review, savedReview);
    }

    @Test
    void addReviewForNonExistingWine() {
        when(wineService.findById(nonExistingWineId)).thenThrow(new NotFoundException(new Wine(), nonExistingWineId));

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.add(nonExistingWineId, review));

        assertEquals(e.getMessage(), "Could not find wine with id " + nonExistingWineId);
        verify(wineService, times(1)).findById(nonExistingWineId);
        verify(reviewRepository, times(0)).save(review);
    }

    @Test
    void editReview() {
        when(userService.isUserAllowedToEdit(review)).thenReturn(true);

        Review editedReview = service
                .edit(review.getId(), review)
                .get();

        verify(reviewRepository, times(1)).findById(review.getId());
        verify(userService, times(1)).isUserAllowedToEdit(review);
        verify(reviewRepository, times(1)).save(review);
        assertEquals(review, editedReview);
    }

    @Test
    void editNonExistingReview() {
        assertTrue(service
                .edit(nonExistingReviewId, review)
                .isEmpty());
        verify(reviewRepository, times(1)).findById(nonExistingReviewId);
        verify(reviewRepository, times(0)).save(review);
    }

    @Test
    void editReviewWithoutPermission() {
        ForbiddenException e = assertThrows(ForbiddenException.class, () ->
                service.edit(review.getId(), review));

        assertEquals("Tried to modify a review that you do not own!", e.getMessage());
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(userService, times(1)).isUserAllowedToEdit(review);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    void deleteReview() {
        when(userService.isUserAllowedToEdit(review)).thenReturn(true);
        service.delete(review.getId());

        verify(userService, times(1)).isUserAllowedToEdit(review);
        verify(reviewRepository, times(1)).deleteById(review.getId());
    }

    @Test
    void shouldThrowErrorWhenWrongUserTriesToDelete() {
        Exception e = assertThrows(ForbiddenException.class, () -> service.delete(review.getId()));
        assertEquals("Tried to modify a review that you do not own!", e.getMessage());
    }

    @Test
    void count() {
        when(reviewRepository.count()).thenReturn((long) reviews.size());
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
        List<String> monthRange = List.of("2020-01", "2020-02");
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
        List<String> monthRange = List.of("2020-01-01", "2020-02");
        String expectedErrorMessage = "Could not parse date 2020-01-01";
        testInvalidDates(monthRange, expectedErrorMessage);
    }

    @Test
    void invalidDateRangeThrowsException2() {
        List<String> monthRange = List.of("2020-01");
        String expectedErrorMessage = "Date range must have a start date and an end date. Given [2020-01]";
        testInvalidDates(monthRange, expectedErrorMessage);
    }

    private void testInvalidDates(List<String> monthRange, String expectedErrorMessage) {
        Exception e = assertThrows(InvalidDateException.class, () ->
                service.search(author, monthRange, ratingRange));

        assertEquals(expectedErrorMessage, e.getMessage());
        verify(reviewRepository, times(0)).findAll(any(ReviewSpecification.class));
    }

    private void isAllowedToEdit(boolean isAllowed) {
        when(userService.isUserAllowedToEdit(review))
                .thenReturn(isAllowed);

        assertEquals(service.isAllowedToEdit(review.getId()), isAllowed);
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(userService, times(1)).isUserAllowedToEdit(review);
    }
}