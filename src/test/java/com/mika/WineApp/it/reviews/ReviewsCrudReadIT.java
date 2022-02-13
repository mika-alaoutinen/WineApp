package com.mika.WineApp.it.reviews;

import com.mika.WineApp.it.IntegrationTestRead;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
class ReviewsCrudReadIT {
    private static final String ENDPOINT = "/reviews";

    @Autowired
    private MockMvc mvc;

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
                .perform(get(ENDPOINT + "/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Mika"))
                .andExpect(jsonPath("$.rating").value(3.0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"7", "8"})
    @WithUserDetails("test_user")
    void isReviewEditable(String reviewId) throws Exception {
        mvc
                .perform(get(String.format("%s/%s/editable", ENDPOINT, reviewId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
