package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

    @Mock
    protected ReviewRepository reviewRepository;

    @Mock
    protected WineRepository wineRepository;

    protected final List<Review> reviews = TestData.initReviews();
    protected final List<Wine> wines = TestData.initWines();
    protected Review review;
    protected Wine wine;

    @BeforeEach
    void init() {
        this.review = reviews.stream().findAny().orElse(null);
        this.wine = wines.stream().findAny().orElse(null);
    }
}
