package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
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

    private List<Review> reviews;
    private Wine wine;
    private final String url = "/reviews";

    @BeforeEach
    public void setUp() {
        // New wine:
        var description = List.of("puolikuiva", "sitruunainen", "yrttinen");
        var foodPairings = List.of("kala", "kasvisruoka", "seurustelujuoma");
        wine = new Wine("Valkoviini", WineType.WHITE, "Espanja", 8.75, 0.75, description, foodPairings, "invalid");
        wine.setId(1L);

        // New reviews:
        var date1 = LocalDate.of(2019, 11, 14);
        var date2 = LocalDate.of(2019, 11, 15);

        Review r1 = new Review("Mika", date1, "Mikan uusi arvostelu", 3.0, wine);
        r1.setId(21L);
        r1.setWine(wine);

        Review r2 = new Review("Salla", date2, "Sallan uusi arvostelu", 4.5, wine);
        r2.setId(22L);
        r2.setWine(wine);

        reviews = List.of(r1, r2);
    }

    @Test
    public void findAll() throws Exception {
        Mockito.when(repository.findAllByOrderByDateDesc()).thenReturn(reviews);

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
        Mockito.when(wineRepository.findById(wine.getId()))
               .thenReturn(Optional.of(wine));

        mvc.perform(MockMvcRequestBuilders
                .post(url + "/{wineId}", wine.getId())
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
    public void search() throws Exception {
        mvc.perform(MockMvcRequestBuilders
           .get(url + "/search"))
           .andExpect(MockMvcResultMatchers.status().isOk());
    }
}