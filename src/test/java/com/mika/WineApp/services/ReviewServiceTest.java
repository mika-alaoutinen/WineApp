package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository repository;

    @Mock
    private WineRepository wineRepository;

    @InjectMocks
    private ReviewServiceImpl service;

    private final List<Review> reviews = TestData.initReviews();
    private final List<Wine> wines = TestData.initWines();
    private Review review;
    private Wine wine;

    @BeforeEach
    void init() {
        this.review = reviews.stream()
                .findAny()
                .orElse(null);

        this.wine = wines.stream()
                .findAny()
                .orElse(null);
    }

    @Test
    public void findAll() {
        var sortedReviews = reviews.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Review::getDate)))
                .collect(Collectors.toList());

        Mockito.when(repository.findAllByOrderByDateDesc())
               .thenReturn(sortedReviews);

        var foundReviews = service.findAll();

        Mockito.verify(repository, Mockito.times(1)).findAllByOrderByDateDesc();
        assertEquals(sortedReviews, foundReviews);
    }

    @Test
    public void findById() {
        long id = review.getId();

        Mockito.when(repository.findById(id))
               .thenReturn(Optional.ofNullable(review));

        Review foundReview = service.findById(id).orElse(null);

        Mockito.verify(repository, Mockito.times(1)).findById(id);
        assertEquals(review, foundReview);
    }

    @Test
    void addReview() {
        long wineId = wine.getId();
        Review newReview = new Review("Mika", LocalDate.now(), "Lisätty arvostelu", 2.0, wine);

        Mockito.when(wineRepository.findById(wineId))
               .thenReturn(Optional.ofNullable(wine));

        Mockito.when(repository.save(newReview))
               .thenReturn(newReview);

        Review savedReview = service.add(wineId, newReview);

        Mockito.verify(wineRepository, Mockito.times(1)).findById(wineId);
        Mockito.verify(repository, Mockito.times(1)).save(newReview);
        assertEquals(newReview, savedReview);
    }

    @Test
    public void editReview() {
        long id = review.getId();

        Mockito.when(repository.findById(id))
               .thenReturn(Optional.ofNullable(review));

        Mockito.when(repository.save(review))
               .thenReturn(review);

        Review editedReview = service.edit(id, review);

        Mockito.verify(repository, Mockito.times(1)).findById(id);
        Mockito.verify(repository, Mockito.times(1)).save(review);
        assertEquals(review, editedReview);
    }

    @Test
    public void deleteReview() {
        long id = review.getId();
        service.delete(id);

        Mockito.verify(repository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void count() {
        Mockito.when(repository.count())
               .thenReturn((long) reviews.size());

        long reviewCount = service.count();

        Mockito.verify(repository, Mockito.times(1)).count();
        assertEquals(reviews.size(), reviewCount);
    }

    @Test
    public void searchWithNullParameters() {
        var result = service.search(null, null, null);
        assertTrue(result.isEmpty());
    }
}