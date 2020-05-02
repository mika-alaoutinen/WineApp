package com.mika.WineApp.controllers;

import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReviewController.class)
public class ReviewControllerMvcTest extends ControllerMvcTest {
    private final static String url = "/reviews";

    @Test
    public void findAll() throws Exception {
        Mockito.when(reviewRepository.findAllByOrderByDateDesc())
               .thenReturn(reviews);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                    .get(url)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertFalse(response.isEmpty());
    }

    @Test
    public void findOne() throws Exception {
        Mockito.when(reviewRepository.findById(review.getId()))
               .thenReturn(Optional.of(review));

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .get(url + "/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Review foundReview = getReviewFromResult(result);
        assertEquals(review, foundReview);
    }

    @Test
    public void addReview() throws Exception {
        Wine wine = wines.stream()
                .findAny()
                .orElse(null);

        assert wine != null;
        Mockito.when(wineRepository.findById(wine.getId()))
               .thenReturn(Optional.of(wine));

        Mockito.when(reviewRepository.save(review))
                .thenReturn(review);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .post(url + "/{wineId}", wine.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Review addedReview = getReviewFromResult(result);
        assertEquals(review, addedReview);
    }

    @Test
    public void editReview() throws Exception {
        Mockito.when(reviewRepository.findById(review.getId()))
               .thenReturn(Optional.of(review));

        Mockito.when(reviewRepository.save(review))
               .thenReturn(review);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .put(url + "/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Review editedReview = getReviewFromResult(result);
        assertEquals(review, editedReview);
    }

    @Test
    public void deleteReview() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(url + "/{id}", review.getId()))
           .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void count() throws Exception {
        Mockito.when(reviewRepository.count()).thenReturn(2L);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/count"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertEquals(2, Integer.parseInt(response));
    }

    @Test
    public void findNewest() throws Exception {
        Mockito.when(reviewRepository.findAllDistinctByOrderByDateDesc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/newest");
        assertEquals(reviews, reviewList);
    }

    @Test
    public void findBest() throws Exception {
        Mockito.when(reviewRepository.findAllByOrderByRatingDesc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/best");
        assertEquals(reviews, reviewList);
    }

    @Test
    public void findWorst() throws Exception {
        Mockito.when(reviewRepository.findAllByOrderByRatingAsc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches(url + "/search/worst");
        assertEquals(reviews, reviewList);
    }

    @Test
    public void search() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(url + "/search"))
           .andExpect(MockMvcResultMatchers.status().isOk());
    }
}