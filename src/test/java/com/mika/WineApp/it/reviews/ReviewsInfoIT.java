package com.mika.WineApp.it.reviews;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestRead;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
class ReviewsInfoIT {
    private static final String ENDPOINT = "/reviews";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setupRepositories() {
        userRepository.saveAll(TestData.initTestUsers());
        reviewRepository.saveAll(TestData.initReviews());
    }
    
    @Test
    void getReviewCount() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }

}
