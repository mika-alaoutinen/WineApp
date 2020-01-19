package com.mika.WineApp.services;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository repository;

    @Mock
    private WineRepository wineRepository;

    @InjectMocks
    private ReviewServiceImpl service;

    private List<Review> reviewList;
    private Wine wine;

    @BeforeEach
    void setUp() {
        // New wine:
        var description = List.of("puolikuiva", "sitruunainen", "yrttinen");
        var foodPairings = List.of("kala", "kasvisruoka", "seurustelujuoma");
        wine = new Wine("Valkoviini", WineType.WHITE, "Espanja", 8.75, 0.75, description, foodPairings, "invalid");
        wine.setId(1L);

        // New reviews:
        var date1 = LocalDate.of(2019, 11, 14);
        var date2 = LocalDate.of(2019, 11, 15);

        Review r1 = new Review("Mika", date1, "Mikan uusi arvostelu", 3.0, wine);
        r1.setId(21L);
        r1.setWine(wine);

        Review r2 = new Review("Salla", date2, "Sallan uusi arvostelu", 4.5, wine);
        r2.setId(22L);
        r2.setWine(wine);

        reviewList = List.of(r1, r2);
    }

    @Test
    void add() {
        Review newReview = new Review("Mika", LocalDate.now(), "Lisätty arvostelu", 2.0, wine);

        Mockito.when(wineRepository.findById(wine.getId()))
               .thenReturn(Optional.of(wine));

        Mockito.when(repository.save(newReview))
               .thenReturn(newReview);

        Review saved = service.add(wine.getId(), newReview);
        assertSame(newReview.getId(), saved.getId());
    }
}