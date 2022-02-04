package com.mika.WineApp.it.reviews;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewsInfoIT extends ReviewTest {
    private static final String ENDPOINT = "/reviews";

    @Test
    void getReviewCount() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }
}
