package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
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

import java.util.List;
import java.util.Optional;

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
    private final String url = "/wines";
    private Wine wine;

    @BeforeEach
    public void initWine() {
        this.wine = wines.stream()
                .findAny()
                .orElse(null);
    }

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
        Mockito.when(repository.findById(wine.getId()))
                .thenReturn(Optional.of(wine));

        Mockito.when(repository.save(wine))
                .thenReturn(wine);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wine)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Wine addedWine = getWineFromResults(result);
        Assertions.assertEquals(wine, addedWine);
    }

    @Test
    public void editWine() throws Exception {
        Mockito.when(repository.findById(wine.getId()))
                .thenReturn(Optional.of(wine));

        Mockito.when(repository.save(wine))
               .thenReturn(wine);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .put(url + "/{id}", wine.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wine)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Wine editedWine = getWineFromResults(result);
        Assertions.assertEquals(wine, editedWine);
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

        var response = testKeywordSearch("/countries");
        Assertions.assertEquals(countries, response);
    }

    @Test
    public void getDescriptions() throws Exception {
        var descriptions = List.of("puolikuiva", "sitruunainen", "yrttinen");

        Mockito.when(repository.findAllDescriptions())
               .thenReturn(descriptions);

        var response = testKeywordSearch("/descriptions");
        Assertions.assertEquals(descriptions, response);
    }

    @Test
    public void getFoodPairings() throws Exception {
        var foodPairings = List.of("kana", "kala", "seurustelujuoma");

        Mockito.when(repository.findAllFoodPairings())
               .thenReturn(foodPairings);

        var response = testKeywordSearch("/food-pairings");
        Assertions.assertEquals(foodPairings, response);
    }

    @Test
    public void search() throws Exception {
        mvc.perform(MockMvcRequestBuilders
           .get(url + "/search"))
           .andExpect(MockMvcResultMatchers.status().isOk());
    }

// Helper methods:

    /**
     * Reads the result from controller and maps it into a Wine object.
     * @param result from controller.
     * @return Wine.
     * @throws Exception ex.
     */
    private Wine getWineFromResults(MvcResult result) throws Exception {
        String response = TestUtilities.getResponseString(result);
        return objectMapper.readValue(response, Wine.class);
    }

    /**
     * Tests the endpoints for retrieving lists of distinct keywords used as wine attributes.
     * E.g. get countries, descriptions or food pairings.
     * @param urlPath endpoint.
     * @return List of distinct keywords used in wines.
     * @throws Exception ex.
     */
    private List<String> testKeywordSearch(String urlPath) throws Exception {
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + urlPath))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        return TestUtilities.parseWordsFromResponse(result);
    }
}