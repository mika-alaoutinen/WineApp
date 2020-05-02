package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.configuration.TestConfig;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@AutoConfigureMockMvc
public abstract class ControllerMvcTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ReviewRepository reviewRepository;

    @MockBean
    protected WineRepository wineRepository;

    protected final List<Review> reviews = TestData.initReviews();
    protected final List<Wine> wines = TestData.initWines();

    protected Review review;
    protected Wine wine;

    @BeforeEach
    void init() {
        this.wine = wines.stream().findAny().orElse(null);
        this.review = reviews.stream().findAny().orElse(null);
    }

// Helper methods:

    /**
     * Reads the result from controller and maps it into a Review object.
     * @param result from controller.
     * @return Review.
     * @throws Exception ex.
     */
    protected Review getReviewFromResult(MvcResult result) throws Exception {
        String response = TestUtilities.getResponseString(result);
        return objectMapper.readValue(response, Review.class);
    }

    /**
     * Reads the result from controller and maps it into a Wine object.
     * @param result from controller.
     * @return Wine.
     * @throws Exception ex.
     */
    protected Wine getWineFromResults(MvcResult result) throws Exception {
        String response = TestUtilities.getResponseString(result);
        return objectMapper.readValue(response, Wine.class);
    }

    /**
     * Used to query the different quick search endpoints.
     * @param url as string.
     * @return List of reviews.
     * @throws Exception ex.
     */
    protected List<Review> quickSearches(String url) throws Exception {
        MvcResult result = doGetRequest(url);
        String response = TestUtilities.getResponseString(result);
        return List.of(objectMapper.readValue(response, Review[].class));
    }

    /**
     * Tests the endpoints for retrieving lists of distinct keywords used as wine attributes.
     * E.g. get countries, descriptions or food pairings.
     * @param url endpoint.
     * @return List of distinct keywords used in wines.
     * @throws Exception ex.
     */
    protected List<String> testKeywordSearch(String url) throws Exception {
        MvcResult result = doGetRequest(url);
        return TestUtilities.parseWordsFromResponse(result);
    }

    // Helper methods:
    private MvcResult doGetRequest(String url) throws Exception {
        return mvc
                .perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
