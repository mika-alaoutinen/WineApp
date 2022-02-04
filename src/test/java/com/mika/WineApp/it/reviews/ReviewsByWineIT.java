package com.mika.WineApp.it.reviews;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestRead;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
class ReviewsByWineIT {
    private static final String ENDPOINT = "/reviews/wine";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    void setupRepositories() {
        userRepository.saveAll(TestData.initTestUsers());
        reviewRepository.saveAll(TestData.initReviews());
    }

    @Test
    void getReviewByWineId() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/id/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].author").value("Mika"));
    }

    @Test
    void getReviewByWineName() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/name/Valkoviini"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].author").value("Mika"))
                .andExpect(jsonPath("$[1].author").value("Salla"));
    }
}
