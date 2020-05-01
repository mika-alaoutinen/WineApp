package com.mika.WineApp.services;

import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.services.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest extends ServiceTest {

    @InjectMocks
    private ReviewServiceImpl service;

    @Test
    public void findAll() {
        var sortedReviews = reviews.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Review::getDate)))
                .collect(Collectors.toList());

        Mockito.when(reviewRepository.findAllByOrderByDateDesc())
               .thenReturn(sortedReviews);

        var foundReviews = service.findAll();

        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByOrderByDateDesc();
        assertEquals(sortedReviews, foundReviews);
    }

    @Test
    public void findById() {
        long id = review.getId();

        Mockito.when(reviewRepository.findById(id))
               .thenReturn(Optional.ofNullable(review));

        Review foundReview = service.findById(id);

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(id);
        assertEquals(review, foundReview);
    }

    @Test
    public void findByNonExistingId() {
        long id = 1L;

        Mockito.when(reviewRepository.findById(id))
                .thenReturn(Optional.empty());

        Exception e = assertThrows(NotFoundException.class, () -> service.findById(id));
        assertEquals(e.getMessage(), "Error: could not find review with id " + id);
        Mockito.verify(reviewRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void addReview() {
        long wineId = wine.getId();
        Review newReview = new Review("Mika", LocalDate.now(), "Lisätty arvostelu", 2.0, wine);

        Mockito.when(wineRepository.findById(wineId))
               .thenReturn(Optional.ofNullable(wine));

        Mockito.when(reviewRepository.save(newReview))
               .thenReturn(newReview);

        Review savedReview = service.add(wineId, newReview);

        Mockito.verify(wineRepository, Mockito.times(1)).findById(wineId);
        Mockito.verify(reviewRepository, Mockito.times(1)).save(newReview);
        assertEquals(newReview, savedReview);
    }

    @Test
    public void addReviewForNonExistingWine() {
        long wineId = 1L;

        Mockito.when(wineRepository.findById(wineId))
                .thenReturn(Optional.empty());

        Exception e = assertThrows(NotFoundException.class, () -> service.add(wineId, review));
        assertEquals(e.getMessage(), "Error: could not find wine with id " + wineId);
        Mockito.verify(wineRepository, Mockito.times(1)).findById(wineId);
        Mockito.verify(reviewRepository, Mockito.times(0)).save(review);
    }

    @Test
    public void editReview() {
        long id = review.getId();

        Mockito.when(reviewRepository.findById(id))
               .thenReturn(Optional.ofNullable(review));

        Mockito.when(reviewRepository.save(review))
               .thenReturn(review);

        Review editedReview = service.edit(id, review);

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(id);
        Mockito.verify(reviewRepository, Mockito.times(1)).save(review);
        assertEquals(review, editedReview);
    }

    @Test
    public void editNonExistingReview() {
        long id = 1L;

        Mockito.when(reviewRepository.findById(id))
                .thenReturn(Optional.empty());

        Exception e = assertThrows(NotFoundException.class, () -> service.edit(id, review));
        assertEquals(e.getMessage(), "Error: could not find review with id " + id);
        Mockito.verify(reviewRepository, Mockito.times(1)).findById(id);
        Mockito.verify(reviewRepository, Mockito.times(0)).save(review);
    }

    @Test
    public void deleteReview() {
        long id = review.getId();
        service.delete(id);

        Mockito.verify(reviewRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void count() {
        Mockito.when(reviewRepository.count())
               .thenReturn((long) reviews.size());

        long reviewCount = service.count();

        Mockito.verify(reviewRepository, Mockito.times(1)).count();
        assertEquals(reviews.size(), reviewCount);
    }

    @Test
    public void searchWithNullParameters() {
        var result = service.search(null, null, null);
        assertTrue(result.isEmpty());
    }
}