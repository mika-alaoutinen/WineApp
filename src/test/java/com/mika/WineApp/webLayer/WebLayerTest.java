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

	@Test
	void contextLoads() {
        Assertions.assertThat(wineController).isNotNull();
        Assertions.assertThat(reviewController).isNotNull();
    }

    @Test
    public void shouldReturnWines() {
	    Assertions.assertThat(restTemplate.getForObject(
	            "http://localhost:" + port + "/api/wines", String.class))
                .isNotBlank();
    }
}