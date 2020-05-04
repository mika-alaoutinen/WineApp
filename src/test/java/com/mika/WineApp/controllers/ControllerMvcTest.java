package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.configuration.TestConfig;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.superclasses.EntityModel;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.security.SecurityUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected SecurityUtilities securityUtilities;

    protected static final String TEST_USER = "test_user";
    protected final List<Review> reviews = TestData.initReviews();
    protected final List<Wine> wines = TestData.initWines();
    protected final List<User> users = TestData.createTestUsers();

    protected Review review;
    protected Wine wine;
    protected User admin = users.get(1);

    @BeforeEach
    void setupTests() {
        this.wine = wines.stream().findAny().orElse(null);
        this.review = reviews.stream().findAny().orElse(null);

        // Review repository mocks:
        assert review != null;
        Mockito.when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));

        Mockito.when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);

        // Wine repository mocks:
        Mockito.when(wineRepository.findById(wine.getId()))
                .thenReturn(Optional.of(wine));

        Mockito.when(wineRepository.save(any(Wine.class)))
                .thenReturn(wine);

        // User repository mocks:
        Mockito.when(userRepository.findByUsername(TEST_USER))
                .thenReturn(Optional.ofNullable(admin));

        // Security utilities mocks:
        Mockito.when(securityUtilities.getUsernameFromSecurityContext())
               .thenReturn(TEST_USER);

        Mockito.when(securityUtilities.isUpdateRequestValid(any(EntityModel.class)))
                .thenReturn(true);
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
        System.out.println(url);
        MvcResult result = doGetRequest(url);
        return TestUtilities.parseWordsFromResponse(result);
    }

    // Helper methods:
    private MvcResult doGetRequest(String url) throws Exception {
        return mvc
                .perform(get(url))
                .andExpect(status().isOk())
                .andReturn();
    }
}
