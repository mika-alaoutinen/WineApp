package com.mika.WineApp.it.reviews;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewsByWineIT extends ReviewTest {
    private static final String ENDPOINT = "/reviews/wine";

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
