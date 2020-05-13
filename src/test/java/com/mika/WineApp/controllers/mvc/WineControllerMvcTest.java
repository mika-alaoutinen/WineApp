package com.mika.WineApp.controllers.mvc;

import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.wine.Wine;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WineControllerMvcTest extends ControllerMvcTest {
    private final static String url = "/wines";

    @Test
    @WithUserDetails(TEST_USER)
    public void findAll() throws Exception {
        Mockito.when(wineRepository.findAllByOrderByNameAsc())
               .thenReturn(wines);

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
    @WithUserDetails(TEST_USER)
    public void findOne() throws Exception {
        MvcResult result = mvc
            .perform(
                get(url + "/{id}", wine.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        Wine foundWine = getWineFromResults(result);
        assertEquals(wine, foundWine);
    }

    @Test
    @WithUserDetails(TEST_USER)
    public void addWine() throws Exception {
        MvcResult result = mvc
            .perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wine)))
            .andExpect(status().isCreated())
            .andReturn();

        Wine addedWine = getWineFromResults(result);
        assertEquals(wine, addedWine);
    }

    @Test
    @WithUserDetails(TEST_USER)
    public void editWine() throws Exception {
        Mockito.when(wineRepository.save(wine))
               .thenReturn(wine);

        MvcResult result = mvc
            .perform(
                put(url + "/{id}", wine.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wine)))
            .andExpect(status().isOk())
            .andReturn();

        Wine editedWine = getWineFromResults(result);
        assertEquals(wine, editedWine);
    }

    @Test
    @WithUserDetails(TEST_USER)
    public void deleteWine() throws Exception {
        mvc.perform(
                delete(url + "/{id}", wine.getId()))
           .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(TEST_USER)
    public void count() throws Exception {
        Mockito.when(wineRepository.count()).thenReturn(1L);

        MvcResult result = mvc
                .perform(
                    get(url + "/count"))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertEquals(1, Integer.parseInt(response));
    }

    @Test
    @WithUserDetails(TEST_USER)
    public void getCountries() throws Exception {
        var countries = List.of("Espanja", "Italia", "Ranska");

        Mockito.when(wineRepository.findAllCountries())
               .thenReturn(countries);

        var response = testKeywordSearch(url + "/countries");
        assertEquals(countries, response);
    }

    @Test
    @WithUserDetails(TEST_USER)
    public void getDescriptions() throws Exception {
        var descriptions = List.of("puolikuiva", "sitruunainen", "yrttinen");

        Mockito.when(wineRepository.findAllDescriptions())
               .thenReturn(descriptions);

        var response = testKeywordSearch(url + "/descriptions");
        assertEquals(descriptions, response);
    }

    @Test
    @WithUserDetails(TEST_USER)
    public void getFoodPairings() throws Exception {
        var foodPairings = List.of("kana", "kala", "seurustelujuoma");

        Mockito.when(wineRepository.findAllFoodPairings())
               .thenReturn(foodPairings);

        var response = testKeywordSearch(url + "/food-pairings");
        assertEquals(foodPairings, response);
    }

    @Test
    @WithUserDetails(TEST_USER)
    public void search() throws Exception {
        mvc.perform(
                get(url + "/search"))
           .andExpect(status().isOk());
    }
}