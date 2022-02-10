package com.mika.WineApp.it.reviews;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewsCrudReadIT extends ReviewTest {
    private static final String ENDPOINT = "/reviews";

    @Test
    void getAllReviews() throws Exception {
        mvc
                .perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getReviewById() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Mika"))
                .andExpect(jsonPath("$.rating").value(3.0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2", "3"})
    @WithUserDetails("test_user")
    void isReviewEditable(String reviewId) throws Exception {
        mvc
                .perform(get(String.format("%s/%s/editable", ENDPOINT, reviewId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
