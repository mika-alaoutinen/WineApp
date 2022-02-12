package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.wines.WineRepository;
import com.mika.WineApp.wines.model.Wine;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

    public static final Long nonExistingReviewId = 3L;
    public static final Long nonExistingWineId = 4L;
    public static final List<Review> reviews = TestData.initReviews();
    public static final List<Wine> wines = TestData.initWines();

    public Review review;
    public Wine wine;

    @Mock
    protected ReviewRepository reviewRepository;

    @Mock
    protected WineRepository wineRepository;

    @Mock
    protected UserService userService;

    @Mock
    protected WineService wineService;
}
