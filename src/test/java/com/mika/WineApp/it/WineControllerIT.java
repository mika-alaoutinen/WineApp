package com.mika.WineApp.it;

import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.wine.Wine;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static com.mika.WineApp.it.ControllerIT.TEST_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(TEST_USER)
class WineControllerIT extends ControllerIT {
    private final static String url = "/wines";

    @Test
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
    void findOne() throws Exception {
        MvcResult result = mvc
                .perform(get(url + "/{id}", wine.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Wine foundWine = getWineFromResults(result);
        assertEquals(wine, foundWine);
    }

    @Test
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
    void deleteWine() throws Exception {
        mvc
                .perform(delete(url + "/{id}", wine.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void count() throws Exception {
        Mockito
                .when(wineRepository.count())
                .thenReturn(1L);

        MvcResult result = mvc
                .perform(get(url + "/count"))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertEquals(1, Integer.parseInt(response));
    }

    @Test
    void isAllowedToEdit() throws Exception {
        Mockito
                .when(wineRepository.findById(wine.getId()))
                .thenReturn(Optional.of(wine));

        MvcResult result = mvc
                .perform(get(url + "/{id}/editable", wine.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    void getCountries() throws Exception {
        var countries = List.of("Espanja", "Italia", "Ranska");

        Mockito
                .when(wineRepository.findAllCountries())
                .thenReturn(countries);

        var response = testKeywordSearch(url + "/countries");
        assertEquals(countries, response);
    }

    @Test
    void getDescriptions() throws Exception {
        var descriptions = List.of("puolikuiva", "sitruunainen", "yrttinen");

        Mockito
                .when(wineRepository.findAllDescriptions())
                .thenReturn(descriptions);

        var response = testKeywordSearch(url + "/descriptions");
        assertEquals(descriptions, response);
    }

    @Test
    void getFoodPairings() throws Exception {
        var foodPairings = List.of("kana", "kala", "seurustelujuoma");

        Mockito
                .when(wineRepository.findAllFoodPairings())
                .thenReturn(foodPairings);

        var response = testKeywordSearch(url + "/food-pairings");
        assertEquals(foodPairings, response);
    }

    @Test
    void validateWineName() throws Exception {
        Mockito
                .when(wineRepository.existsByName(Mockito.anyString()))
                .thenReturn(false);

        MvcResult result = mvc
                .perform(get(url + "/validate?name=wine name"))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    void search() throws Exception {
        mvc
                .perform(get(url + "/search"))
                .andExpect(status().isOk());
    }
}