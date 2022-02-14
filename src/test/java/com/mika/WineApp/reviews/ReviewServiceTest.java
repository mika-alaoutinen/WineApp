package com.mika.WineApp.reviews;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.authentication.UserAuthentication;
import com.mika.WineApp.entities.Review;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.errors.ForbiddenException;
import com.mika.WineApp.errors.InvalidDateException;
import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.services.WineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    private static final List<Review> REVIEWS = TestData.initReviews();
    private static final String author = "Mika";
    private static final List<Double> ratingRange = List.of(1.0, 5.0);

    @Mock
    private ReviewRepository repository;

    @Mock
    private UserAuthentication userAuth;

    @Mock
    private WineService wineService;

    @InjectMocks
    private ReviewServiceImpl service;

    @Test
    void findAll() {
        var sortedReviews = REVIEWS
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Review::getDate)))
                .collect(Collectors.toList());

        when(repository.findAllByOrderByDateDesc()).thenReturn(sortedReviews);
        var foundReviews = service.findAll();

        verify(repository, times(1)).findAllByOrderByDateDesc();
        assertEquals(sortedReviews, foundReviews);
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Review()));
        assertFalse(service
                .findById(1L)
                .isEmpty());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findByNonExistingId() {
        long nonExistingReviewId = 3L;
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertTrue(service
                .findById(nonExistingReviewId)
                .isEmpty());

        verify(repository, times(1)).findById(nonExistingReviewId);
    }

    @Test
    void findByWineId() {
        when(repository.findByWineId(anyLong())).thenReturn(REVIEWS);
        var foundReviews = service.findByWineId(1L);
        assertEquals(2, foundReviews.size());
        verify(repository, times(1)).findByWineId(1L);
    }

    @Test
    void findByWineIdReturnsEmptyList() {
        when(repository.findByWineId(anyLong())).thenReturn(List.of());
        var foundReviews = service.findByWineId(1L);
        assertTrue(foundReviews.isEmpty());
    }

    @Test
    void findByWineName() {
        String wineName = "Valkoviini 1";
        when(repository.findByWineNameContainingIgnoreCase(wineName)).thenReturn(REVIEWS);
        var foundReviews = service.findByWineName(wineName);
        assertEquals(2, foundReviews.size());
    }

    @Test
    void addReview() {
        when(userAuth.setUser(any(Review.class))).thenReturn(new Review());
        when(wineService.findById(anyLong())).thenReturn(Optional.of(new Wine()));
        when(repository.save(any(Review.class))).thenReturn(new Review());

        service.add(1L, new Review());
        verify(wineService, times(1)).findById(1L);
        verify(userAuth, times(1)).setUser(any(Review.class));
        verify(repository, times(1)).save(any(Review.class));
    }

    @Test
    void addReviewForNonExistingWine() {
        long wineId = 13L;
        when(wineService.findById(anyLong())).thenThrow(new NotFoundException(new Wine(), wineId));

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.add(13L, new Review()));

        assertEquals(e.getMessage(), "Could not find wine with id " + wineId);
        verify(wineService, times(1)).findById(wineId);
        verify(repository, never()).save(any(Review.class));
    }

    @Test
    void editReview() {
        Review review = Review
                .builder()
                .author("author")
                .date(LocalDate.now())
                .reviewText("text")
                .rating(3.0)
                .build();

        when(repository.findById(anyLong())).thenReturn(Optional.of(review));
        when(userAuth.isUserAllowedToEdit(any(Review.class))).thenReturn(true);
        when(repository.save(any(Review.class))).thenReturn(review);

        service.edit(1L, new Review());
        verify(repository, times(1)).findById(1L);
        verify(userAuth, times(1)).isUserAllowedToEdit(any(Review.class));
        verify(repository, times(1)).save(any(Review.class));
    }

    @Test
    void editNonExistingReview() {
        long nonExistingReviewId = 3L;
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertTrue(service
                .edit(nonExistingReviewId, new Review())
                .isEmpty());

        verify(repository, times(1)).findById(nonExistingReviewId);
        verify(repository, never()).save(any(Review.class));
    }

    @Test
    void editReviewWithoutPermission() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Review()));
        ForbiddenException e = assertThrows(ForbiddenException.class, () -> service.edit(1L, new Review()));

        assertEquals("Tried to modify a review that you do not own!", e.getMessage());
        verify(repository, times(1)).findById(1L);
        verify(userAuth, times(1)).isUserAllowedToEdit(any(Review.class));
        verify(repository, never()).save(any(Review.class));
    }

    @Test
    void deleteReview() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Review()));
        when(userAuth.isUserAllowedToEdit(any(Review.class))).thenReturn(true);

        service.delete(1L);
        verify(userAuth, times(1)).isUserAllowedToEdit(any(Review.class));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowErrorWhenWrongUserTriesToDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Review()));
        Exception e = assertThrows(ForbiddenException.class, () -> service.delete(1L));
        assertEquals("Tried to modify a review that you do not own!", e.getMessage());
    }

    @Test
    void count() {
        when(repository.count()).thenReturn((long) REVIEWS.size());
        long reviewCount = service.count();

        verify(repository, times(1)).count();
        assertEquals(REVIEWS.size(), reviewCount);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isAllowedToEdit(boolean isAllowed) {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Review()));
        when(userAuth.isUserAllowedToEdit(any(Review.class))).thenReturn(isAllowed);

        assertEquals(service.isAllowedToEdit(1L), isAllowed);
        verify(repository, times(1)).findById(1L);
        verify(userAuth, times(1)).isUserAllowedToEdit(any(Review.class));
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
        verify(repository, times(1)).findAll(any(ReviewSpecification.class));
    }

    @Test
    void dateRangeIsNull() {
        service.search(author, null, ratingRange);
        verify(repository, times(1)).findAll(any(ReviewSpecification.class));
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
        verify(repository, never()).findAll(any(ReviewSpecification.class));
    }
}