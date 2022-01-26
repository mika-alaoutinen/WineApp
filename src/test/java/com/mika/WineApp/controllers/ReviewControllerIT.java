package com.mika.WineApp.controllers;

import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.review.Review;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static com.mika.WineApp.controllers.ControllerIT.TEST_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(TEST_USER)
class ReviewControllerIT extends ControllerIT {
    private final static String url = "/reviews";

    @Test
    void findAll() throws Exception {
        Mockito
                .when(reviewRepository.findAllByOrderByDateDesc())
                .thenReturn(reviews);

        MvcResult result = mvc
                .perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertFalse(response.isEmpty());
    }

    @Test
    void findOne() throws Exception {
        MvcResult result = mvc
                .perform(get(url + "/{id}", review.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Review foundReview = getReviewFromResult(result);
        assertEquals(review, foundReview);
    }

    @Test
    void findByWineId() throws Exception {
        Long wineId = review
                .getWine()
                .getId();

        mvc
                .perform(get(url + "/wine/id/{wineId}", wineId))
                .andExpect(status().isOk());

        Mockito
                .verify(reviewRepository)
                .findByWineId(wineId);
    }

    @Test
    void findByWineName() throws Exception {
        String wineName = review
                .getWine()
                .getName();

        mvc
                .perform(get(url + "/wine/name/{wineName}", wineName))
                .andExpect(status().isOk());

        Mockito
                .verify(reviewRepository)
                .findByWineNameContainingIgnoreCase(wineName);
    }

    @Test
    void addReview() throws Exception {
        MvcResult result = mvc
                .perform(
                        post(url + "/{wineId}", wine.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isCreated())
                .andReturn();

        Review addedReview = getReviewFromResult(result);
        assertEquals(review, addedReview);
    }

    @Test
    void editReview() throws Exception {
        MvcResult result = mvc
                .perform(
                        put(url + "/{id}", review.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andReturn();

        Review editedReview = getReviewFromResult(result);
        assertEquals(review, editedReview);
    }

    @Test
    void deleteReview() throws Exception {
        mvc
                .perform(delete(url + "/{id}", review.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void count() throws Exception {
        Mockito
                .when(reviewRepository.count())
                .thenReturn(2L);

        MvcResult result = mvc
                .perform(get(url + "/count"))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertEquals(2, Integer.parseInt(response));
    }

    @Test
    void isAllowedToEdit() throws Exception {
        Mockito
                .when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));

        MvcResult result = mvc
                .perform(get(url + "/{id}/editable", review.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    void findNewest() throws Exception {
        Mockito
                .when(reviewRepository.findAllDistinctByOrderByDateDesc(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/newest");
        assertEquals(reviews, reviewList);
    }

    @Test
    void findBest() throws Exception {
        Mockito
                .when(reviewRepository.findAllByOrderByRatingDesc(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/best");
        assertEquals(reviews, reviewList);
    }

    @Test
    void findWorst() throws Exception {
        Mockito
                .when(reviewRepository.findAllByOrderByRatingAsc(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/worst");
        assertEquals(reviews, reviewList);
    }

    @Test
    void search() throws Exception {
        mvc
                .perform(get(url + "/search"))
                .andExpect(status().isOk());
    }
}