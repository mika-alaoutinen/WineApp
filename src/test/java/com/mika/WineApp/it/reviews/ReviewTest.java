package com.mika.WineApp.it.reviews;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestRead;
import com.mika.WineApp.reviews.ReviewRepository;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.wines.WineRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTestRead
class ReviewTest {

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MockMvc mvc;

    @BeforeAll
    void setupRepositories() {
        userRepository.saveAll(TestData.initTestUsers());

        // Need to save wine to repository, because otherwise reviews would have an unsaved transient dependency
        var wine = TestData
                .initWines()
                .get(0);
        wineRepository.save(wine);

        var reviews = TestData.initReviews();
        reviews.forEach(r -> r.setWine(wine));
        reviewRepository.saveAll(reviews);
    }
}
