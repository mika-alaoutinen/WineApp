package com.mika.WineApp.it.reviews;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewsSearchIT extends ReviewTest {
    private static final String ENDPOINT = "/reviews/search";

    @Test
    void searchReviewsByAuthor() throws Exception {
        mvc
                .perform(get(ENDPOINT + "?author=Mika"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].author").value("Mika"));
    }

    @Test
    void searchReviewsByDateRange() throws Exception {
        mvc
                .perform(get(ENDPOINT + "?dateRange=2019-10,2019-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].date").value("2019-11-15"));
    }

    @Test
    void searchReviewsByRatingRange() throws Exception {
        mvc
                .perform(get(ENDPOINT + "?ratingRange=3.0,4.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].rating").value(3.0));
    }

    @Test
    void searchBestReviews() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/best"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].author").value("Salla"))
                .andExpect(jsonPath("$[0].rating").value(4.5));
    }

    @Test
    void searchWorstReviews() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/worst"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].author").value("Mika"))
                .andExpect(jsonPath("$[0].rating").value(3.0));
    }

    @Test
    void searchNewestReviews() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/newest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].author").value("Salla"))
                .andExpect(jsonPath("$[0].date").value("2019-11-15"));
    }
}
