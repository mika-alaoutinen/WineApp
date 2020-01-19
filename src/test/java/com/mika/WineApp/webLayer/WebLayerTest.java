package com.mika.WineApp.webLayer;

import com.mika.WineApp.controllers.ReviewController;
import com.mika.WineApp.controllers.WineController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

/**
 * Smoke tests for the web layer.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebLayerTest {

    @Autowired
    private ReviewController reviewController;

    @Autowired
    private WineController wineController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String url = "http://localhost:";

	@Test
	void contextLoads() {
        Assertions.assertThat(wineController).isNotNull();
        Assertions.assertThat(reviewController).isNotNull();
    }

    @Test
    public void shouldReturnWines() {
        String wines = restTemplate.getForObject(url + port + "/api/wines", String.class);
	    Assertions.assertThat(wines).isNotBlank();
    }

    @Test
    public void shouldReturnReviews() {
        String reviews = restTemplate.getForObject(url + port + "/api/reviews", String.class);
	    Assertions.assertThat(reviews).isNotBlank();
    }
}