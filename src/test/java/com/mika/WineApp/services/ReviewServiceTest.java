package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.invaliddate.InvalidDateException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.security.SecurityUtilities;
import com.mika.WineApp.services.impl.ReviewServiceImpl;
import com.mika.WineApp.specifications.ReviewSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    private static final String author = "Mika";
    private static final Double[] ratingRange = { 1.0, 5.0 };
    private static final Long nonExistingReviewId = 3L;
    private static final Long nonExistingWineId = 4L;

    private static final List<Review> reviews = TestData.initReviews();
    private static final List<Wine> wines = TestData.initWines();

    private Review review;
    protected Wine wine;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    protected UserService userService;

    @Mock
    private WineService wineService;

    @Mock
    protected SecurityUtilities securityUtils;

    @InjectMocks
    private ReviewServiceImpl service;

    @BeforeEach
    public void setupTests() {
        this.review = reviews.stream().findAny().orElse(null);
        this.wine = wines.stream().findAny().orElse(null);

        Mockito.lenient()
                .when(reviewRepository.findById(nonExistingReviewId))
                .thenReturn(Optional.empty());

        Mockito.lenient()
                .when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.ofNullable(review));

        Mockito.lenient()
                .when(reviewRepository.save(review))
                .thenReturn(review);

        Mockito.lenient()
                .when(securityUtils.isUpdateRequestValid(review))
                .thenReturn(true);
    }

    @Test
    public void findAll() {
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
    public void findById() {
        System.out.println(review.getId());
        Review foundReview = service.findById(review.getId());

        verify(reviewRepository, times(1)).findById(review.getId());
        assertEquals(review, foundReview);
    }

    @Test
    public void findByNonExistingId() {
        Exception e = assertThrows(NotFoundException.class, () -> service.findById(nonExistingReviewId));
        assertEquals(e.getMessage(), "Error: could not find review with id " + nonExistingReviewId);
        verify(reviewRepository, times(1)).findById(nonExistingReviewId);
    }

    @Test
    public void findByWineId() {
        Mockito.when(reviewRepository.findByWineId(review.getId()))
               .thenReturn(reviews);

        var foundReviews = service.findByWineId(review.getId());
        assertEquals(2, foundReviews.size());
    }

    @Test
    public void findByWineIdReturnsEmptyList() {
        long id = 1L;

        Mockito.when(reviewRepository.findByWineId(id))
               .thenReturn(List.of());

        var foundReviews = service.findByWineId(id);
        assertTrue(foundReviews.isEmpty());
    }

    @Test
    public void findByWineName() {
        String wineName = "Valkoviini 1";
        Mockito.when(reviewRepository.findByWineNameContainingIgnoreCase(wineName))
                .thenReturn(reviews);

        var foundReviews = service.findByWineName(wineName);
        assertEquals(2, foundReviews.size());
    }

    @Test
    public void addReview() {
        Review newReview = new Review("Mika", LocalDate.now(), "Lisätty arvostelu", 2.0, wine);

        Mockito.when(reviewRepository.save(newReview))
               .thenReturn(newReview);

        Review savedReview = service.add(wine.getId(), newReview);

        verify(wineService, times(1)).findById(wine.getId());
        verify(reviewRepository, times(1)).save(newReview);
        assertEquals(newReview, savedReview);
    }

    @Test
    public void addReviewForNonExistingWine() {
        Mockito.when(wineService.findById(nonExistingWineId))
               .thenThrow(new NotFoundException(new Wine(), nonExistingWineId));

        Exception e = assertThrows(NotFoundException.class, () -> service.add(nonExistingWineId, review));
        assertEquals(e.getMessage(), "Error: could not find wine with id " + nonExistingWineId);
        verify(wineService, times(1)).findById(nonExistingWineId);
        verify(reviewRepository, times(0)).save(review);
    }

    @Test
    public void editReview() {
        Review editedReview = service.edit(review.getId(), review);
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, times(1)).save(review);
        assertEquals(review, editedReview);
    }

    @Test
    public void editNonExistingReview() {
        Exception e = assertThrows(NotFoundException.class, () -> service.edit(nonExistingReviewId, review));
        assertEquals(e.getMessage(), "Error: could not find review with id " + nonExistingReviewId);
        verify(reviewRepository, times(1)).findById(nonExistingReviewId);
        verify(reviewRepository, times(0)).save(review);
    }

    @Test
    public void deleteReview() {
        service.delete(review.getId());
        verify(reviewRepository, times(1)).deleteById(review.getId());
    }

    @Test
    public void shouldThrowErrorWhenWrongUserTriesToDelete() {
        Mockito.when(securityUtils.isUpdateRequestValid(review))
                .thenReturn(false);

        Exception e = assertThrows(BadRequestException.class, () -> service.delete(review.getId()));
        assertEquals("Error: tried to modify review or wine that you do not own!", e.getMessage());
    }

    @Test
    public void count() {
        Mockito.when(reviewRepository.count())
               .thenReturn((long) reviews.size());

        long reviewCount = service.count();

        verify(reviewRepository, times(1)).count();
        assertEquals(reviews.size(), reviewCount);
    }

    @Test
    public void searchWithNullParameters() {
        var result = service.search(null, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void validDateRangeIsParsed() {
        String[] monthRange = { "2020-01", "2020-02" };
        service.search(author, monthRange, ratingRange);
        verify(reviewRepository, times(1)).findAll(any(ReviewSpecification.class));
    }

    @Test
    public void dateRangeIsNull() {
        service.search(author, null, ratingRange);
        verify(reviewRepository, times(1)).findAll(any(ReviewSpecification.class));
    }

    @Test
    public void invalidDateRangeThrowsException1() {
        String[] monthRange = { "2020-01-01", "2020-02" };
        String expectedErrorMessage = "Error: could not parse date 2020-01-01";
        testInvalidDates(monthRange, expectedErrorMessage);
    }

    @Test
    public void invalidDateRangeThrowsException2() {
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
}