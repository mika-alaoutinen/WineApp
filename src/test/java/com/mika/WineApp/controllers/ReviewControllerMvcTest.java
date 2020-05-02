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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewControllerMvcTest extends ControllerMvcTest {
    private final static String url = "/reviews";

    @Test
    @WithUserDetails()
    public void findAll() throws Exception {
        Mockito.when(reviewRepository.findAllByOrderByDateDesc())
               .thenReturn(reviews);

        MvcResult result = mvc
            .perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertFalse(response.isEmpty());
    }

    @Test
    @WithUserDetails()
    public void findOne() throws Exception {
        Mockito.when(reviewRepository.findById(review.getId()))
               .thenReturn(Optional.of(review));

        MvcResult result = mvc
            .perform(
                get(url + "/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        Review foundReview = getReviewFromResult(result);
        assertEquals(review, foundReview);
    }

    @Test
    @WithUserDetails()
    public void addReview() throws Exception {
        Mockito.when(wineRepository.findById(wine.getId()))
               .thenReturn(Optional.of(wine));

        Mockito.when(reviewRepository.save(review))
                .thenReturn(review);

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
    @WithUserDetails()
    public void editReview() throws Exception {
        Mockito.when(reviewRepository.findById(review.getId()))
               .thenReturn(Optional.of(review));

        Mockito.when(reviewRepository.save(review))
               .thenReturn(review);

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
    @WithUserDetails()
    public void deleteReview() throws Exception {
        mvc.perform(
                delete(url + "/{id}", review.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails()
    public void count() throws Exception {
        Mockito.when(reviewRepository.count()).thenReturn(2L);

        MvcResult result = mvc
            .perform(
                get(url + "/count"))
            .andExpect(status().isOk())
            .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertEquals(2, Integer.parseInt(response));
    }

    @Test
    @WithUserDetails()
    public void findNewest() throws Exception {
        Mockito.when(reviewRepository.findAllDistinctByOrderByDateDesc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/newest");
        assertEquals(reviews, reviewList);
    }

    @Test
    @WithUserDetails()
    public void findBest() throws Exception {
        Mockito.when(reviewRepository.findAllByOrderByRatingDesc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/best");
        assertEquals(reviews, reviewList);
    }

    @Test
    @WithUserDetails()
    public void findWorst() throws Exception {
        Mockito.when(reviewRepository.findAllByOrderByRatingAsc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/worst");
        assertEquals(reviews, reviewList);
    }

    @Test
    @WithUserDetails()
    public void search() throws Exception {
        mvc.perform(
                get(url + "/search"))
           .andExpect(status().isOk());
    }
}