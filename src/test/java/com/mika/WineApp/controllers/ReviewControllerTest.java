package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mika.WineApp.TestData;
import com.mika.WineApp.TestUtilities;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReviewController.class)
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
        Wine wine = wines.get(0);

        Mockito.when(wineRepository.findById(wine.getId()))
               .thenReturn(Optional.of(wine));

        mvc.perform(MockMvcRequestBuilders
                .post(url + "/{wineId}", wines.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviews.get(0))))
           .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void deleteReview() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", reviews.get(0).getId()))
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

    private List<Review> quickSearches(String urlPath) throws Exception {
        MvcResult result = mvc
            .perform(MockMvcRequestBuilders.get(url + urlPath))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String response = TestUtilities.getResponseString(result);
        return List.of(objectMapper.readValue(response, Review[].class));
    }
}