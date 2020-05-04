package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.security.SecurityUtilities;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

    protected static final Long nonExistingReviewId = 3L;
    protected static final Long nonExistingWineId = 4L;
    protected static final List<Review> reviews = TestData.initReviews();
    protected static final List<Wine> wines = TestData.initWines();
    protected static final User user = TestData.createTestUsers().get(0);
    protected Review review;
    protected Wine wine;

    @Mock
    protected UserService userService;

    @Mock
    protected SecurityUtilities securityUtils;
}
