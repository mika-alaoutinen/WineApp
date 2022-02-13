package com.mika.WineApp.it.reviews;

import com.mika.WineApp.it.IntegrationTestRead;
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
    private MockMvc mvc;

    @Test
    void getReviewCount() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }
}
