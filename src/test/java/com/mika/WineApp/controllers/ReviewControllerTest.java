package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReviewController.class)
@EnableWebMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewServiceImpl service;

    @MockBean
    private ReviewRepository repository;

    @MockBean
    WineRepository wineRepository;

    private final List<Review> reviews = TestData.initReviews();
    private final List<Wine> wines = TestData.initWines();
    private final String url = "/reviews";
    private Review review;

    @BeforeEach
    void init () {
        this.review = reviews.stream()
                .findAny()
                .orElse(null);
    }

    @Test
    public void findAll() throws Exception {
        Mockito.when(repository.findAllByOrderByDateDesc())
               .thenReturn(reviews);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                    .get(url)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String response = TestUtilities.getResponseString(result);
        Assertions.assertFalse(response.isEmpty());
    }

    @Test
    public void addReview() throws Exception {
        Wine wine = wines.stream()
                .findAny()
                .orElse(null);

        Mockito.when(wineRepository.findById(wine.getId()))
               .thenReturn(Optional.of(wine));

        Mockito.when(repository.save(review))
                .thenReturn(review);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .post(url + "/{wineId}", wine.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Review addedReview = getReviewFromResult(result);
        Assertions.assertEquals(review, addedReview);
    }

    @Test
    public void editReview() throws Exception {
        Mockito.when(repository.findById(review.getId()))
               .thenReturn(Optional.of(review));

        Mockito.when(repository.save(review))
               .thenReturn(review);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .put(url + "/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Review editedReview = getReviewFromResult(result);
        Assertions.assertEquals(review, editedReview);
    }

    @Test
    public void deleteReview() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(url + "/{id}", review.getId()))
           .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void count() throws Exception {
        Mockito.when(repository.count()).thenReturn(2L);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/count"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        Assertions.assertEquals(2, Integer.parseInt(response));
    }

    @Test
    public void findNewest() throws Exception {
        Mockito.when(repository.findAllDistinctByOrderByDateDesc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches("/search/newest");
        Assertions.assertEquals(reviews, reviewList);
    }

    @Test
    public void findBest() throws Exception {
        Mockito.when(repository.findAllByOrderByRatingDesc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches("/search/best");
        Assertions.assertEquals(reviews, reviewList);
    }

    @Test
    public void findWorst() throws Exception {
        Mockito.when(repository.findAllByOrderByRatingAsc(PageRequest.of(0, 10)))
               .thenReturn(new PageImpl<>(reviews));

        var reviewList = quickSearches("/search/worst");
        Assertions.assertEquals(reviews, reviewList);
    }

    @Test
    public void search() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(url + "/search"))
           .andExpect(MockMvcResultMatchers.status().isOk());
    }

// Helper methods:

    /**
     * Reads the result from controller and maps it into a Review object.
     * @param result from controller.
     * @return Review.
     * @throws Exception ex.
     */
    private Review getReviewFromResult(MvcResult result) throws Exception {
        String response = TestUtilities.getResponseString(result);
        return objectMapper.readValue(response, Review.class);
    }

    /**
     * Used to query the different quick search endpoints.
     * @param urlPath as string.
     * @return List of reviews.
     * @throws Exception ex.
     */
    private List<Review> quickSearches(String urlPath) throws Exception {
        MvcResult result = mvc
            .perform(MockMvcRequestBuilders.get(url + urlPath))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String response = TestUtilities.getResponseString(result);
        return List.of(objectMapper.readValue(response, Review[].class));
    }
}