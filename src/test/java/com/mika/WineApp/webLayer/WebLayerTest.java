package com.mika.WineApp.webLayer;

import com.mika.WineApp.controllers.ReviewCrudController;
import com.mika.WineApp.controllers.WineCrudController;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.repositories.WineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Smoke tests for the web layer.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WebLayerTest {

    @MockBean
    protected ReviewRepository reviewRepository;

    @MockBean
    protected WineRepository wineRepository;

    @MockBean
    protected UserRepository userRepository;

    @Autowired
    private ReviewCrudController reviewController;

    @Autowired
    private WineCrudController wineController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String url = "http://localhost:";

    @Test
    void contextLoads() {
        Assertions
                .assertThat(wineController)
                .isNotNull();
        Assertions
                .assertThat(reviewController)
                .isNotNull();
    }

    @Test
    void shouldReturnWines() {
        String wines = restTemplate.getForObject(url + port + "/api/wines", String.class);
        Assertions
                .assertThat(wines)
                .isNotBlank();
    }

    @Test
    void shouldReturnReviews() {
        String reviews = restTemplate.getForObject(url + port + "/api/reviews", String.class);
        Assertions
                .assertThat(reviews)
                .isNotBlank();
    }
}