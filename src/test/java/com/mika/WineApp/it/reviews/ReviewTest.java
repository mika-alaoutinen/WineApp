package com.mika.WineApp.it.reviews;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestRead;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTestRead
class ReviewTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MockMvc mvc;

    @BeforeAll
    void setupRepositories() {
        userRepository.saveAll(TestData.initTestUsers());
        reviewRepository.saveAll(TestData.initReviews());
    }
}
