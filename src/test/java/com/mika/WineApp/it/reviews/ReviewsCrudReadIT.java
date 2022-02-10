package com.mika.WineApp.it.reviews;

import org.junit.jupiter.api.Test;
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
                .perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Mika"))
                .andExpect(jsonPath("$.rating").value(3.0));
    }

    @Test
    @WithUserDetails("test_user")
    void isReviewEditable() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/1/editable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
