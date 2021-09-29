package com.mika.WineApp.controllers;

import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.wine.Wine;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WineControllerMvcTest extends ControllerMvcTest {
    private final static String url = "/wines";

    @Test
    @WithUserDetails(TEST_USER)
    void findAll() throws Exception {
        Mockito
                .when(wineRepository.findAllByOrderByNameAsc())
                .thenReturn(wines);

        MvcResult result = mvc
                .perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertFalse(response.isEmpty());
    }

    @Test
    @WithUserDetails(TEST_USER)
    void findOne() throws Exception {
        MvcResult result = mvc
                .perform(get(url + "/{id}", wine.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Wine foundWine = getWineFromResults(result);
        assertEquals(wine, foundWine);
    }

    @Test
    @WithUserDetails(TEST_USER)
    void addWine() throws Exception {
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
    void editWine() throws Exception {
        Mockito
                .when(wineRepository.save(wine))
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
    void deleteWine() throws Exception {
        mvc
                .perform(delete(url + "/{id}", wine.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(TEST_USER)
    void count() throws Exception {
        Mockito
                .when(wineRepository.count())
                .thenReturn(1L);

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
    void isAllowedToEdit() throws Exception {
        Wine wineWithUser = wine;
        wineWithUser.setUser(admin);

        Mockito
                .when(wineRepository.findById(wineWithUser.getId()))
                .thenReturn(Optional.of(wineWithUser));

        MvcResult result = mvc
                .perform(
                        get(url + "/{id}/editable", wineWithUser.getId())
                )
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    @WithUserDetails(TEST_USER)
    void getCountries() throws Exception {
        var countries = List.of("Espanja", "Italia", "Ranska");

        Mockito
                .when(wineRepository.findAllCountries())
                .thenReturn(countries);

        var response = testKeywordSearch(url + "/countries");
        assertEquals(countries, response);
    }

    @Test
    @WithUserDetails(TEST_USER)
    void getDescriptions() throws Exception {
        var descriptions = List.of("puolikuiva", "sitruunainen", "yrttinen");

        Mockito
                .when(wineRepository.findAllDescriptions())
                .thenReturn(descriptions);

        var response = testKeywordSearch(url + "/descriptions");
        assertEquals(descriptions, response);
    }

    @Test
    @WithUserDetails(TEST_USER)
    void getFoodPairings() throws Exception {
        var foodPairings = List.of("kana", "kala", "seurustelujuoma");

        Mockito
                .when(wineRepository.findAllFoodPairings())
                .thenReturn(foodPairings);

        var response = testKeywordSearch(url + "/food-pairings");
        assertEquals(foodPairings, response);
    }

    @Test
    @WithUserDetails(TEST_USER)
    void validateWineName() throws Exception {
        Mockito
                .when(wineRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);

        MvcResult result = mvc
                .perform(
                        get(url + "/validate?name=wine name"))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    @WithUserDetails(TEST_USER)
    void search() throws Exception {
        mvc
                .perform(
                        get(url + "/search"))
                .andExpect(status().isOk());
    }
}