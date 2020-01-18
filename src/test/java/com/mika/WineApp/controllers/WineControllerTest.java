package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import org.junit.jupiter.api.Assertions;
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

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WineController.class)
public class WineControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WineService service;

    @MockBean
    private WineRepository repository;

    private final List<Wine> wines = TestData.initWines();
    private final Wine wine = wines.get(0);
    private final String url = "/wines";

    @Test
    public void findAll() throws Exception {
        Mockito.when(repository.findAllByOrderByNameAsc())
               .thenReturn(wines);

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
    public void addWine() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wine)))
           .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void deleteWine() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", wine.getId()))
           .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void count() throws Exception {
        Mockito.when(repository.count())
               .thenReturn(1L);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/count"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        Assertions.assertEquals(1, Integer.parseInt(response));
    }

    @Test
    public void getCountries() throws Exception {
        var countries = List.of("Espanja", "Italia", "Ranska");

        Mockito.when(repository.findAllCountries())
               .thenReturn(countries);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/countries"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = TestUtilities.parseWordsFromResponse(result);
        Assertions.assertEquals(countries, response);
    }

    @Test
    public void getDescriptions() throws Exception {
        var descriptions = List.of("puolikuiva", "sitruunainen", "yrttinen");

        Mockito.when(repository.findAllDescriptions())
               .thenReturn(descriptions);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/descriptions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = TestUtilities.parseWordsFromResponse(result);
        Assertions.assertEquals(descriptions, response);
    }

    @Test
    public void getFoodPairings() throws Exception {
        var foodPairings = List.of("kana", "kala", "seurustelujuoma");

        Mockito.when(repository.findAllFoodPairings())
               .thenReturn(foodPairings);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/food-pairings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = TestUtilities.parseWordsFromResponse(result);
        Assertions.assertEquals(foodPairings, response);
    }

    @Test
    public void search() throws Exception {
        mvc.perform(MockMvcRequestBuilders
           .get(url + "/search"))
           .andExpect(MockMvcResultMatchers.status().isOk());
    }
}