package com.mika.WineApp.controllers;

import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.wine.Wine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WineController.class)
public class WineControllerMvcTest extends ControllerMvcTest {
    private final static String url = "/wines";

    @Test
    public void findAll() throws Exception {
        Mockito.when(wineRepository.findAllByOrderByNameAsc())
               .thenReturn(wines);

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
        Mockito.when(wineRepository.findById(wine.getId()))
               .thenReturn(Optional.of(wine));

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .get(url + "/{id}", wine.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Wine foundWine = getWineFromResults(result);
        assertEquals(wine, foundWine);
    }

    @Test
    public void addWine() throws Exception {
        Mockito.when(wineRepository.findById(wine.getId()))
                .thenReturn(Optional.of(wine));

        Mockito.when(wineRepository.save(wine))
                .thenReturn(wine);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wine)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        Wine addedWine = getWineFromResults(result);
        assertEquals(wine, addedWine);
    }

    @Test
    public void editWine() throws Exception {
        Mockito.when(wineRepository.findById(wine.getId()))
                .thenReturn(Optional.of(wine));

        Mockito.when(wineRepository.save(wine))
               .thenReturn(wine);

        MvcResult result = mvc
            .perform(MockMvcRequestBuilders
                .put(url + "/{id}", wine.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wine)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Wine editedWine = getWineFromResults(result);
        assertEquals(wine, editedWine);
    }

    @Test
    public void deleteWine() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", wine.getId()))
           .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void count() throws Exception {
        Mockito.when(wineRepository.count())
               .thenReturn(1L);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/count"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        assertEquals(1, Integer.parseInt(response));
    }

    @Test
    public void getCountries() throws Exception {
        var countries = List.of("Espanja", "Italia", "Ranska");

        Mockito.when(wineRepository.findAllCountries())
               .thenReturn(countries);

        var response = testKeywordSearch("/countries");
        assertEquals(countries, response);
    }

    @Test
    public void getDescriptions() throws Exception {
        var descriptions = List.of("puolikuiva", "sitruunainen", "yrttinen");

        Mockito.when(wineRepository.findAllDescriptions())
               .thenReturn(descriptions);

        var response = testKeywordSearch("/descriptions");
        assertEquals(descriptions, response);
    }

    @Test
    public void getFoodPairings() throws Exception {
        var foodPairings = List.of("kana", "kala", "seurustelujuoma");

        Mockito.when(wineRepository.findAllFoodPairings())
               .thenReturn(foodPairings);

        var response = testKeywordSearch("/food-pairings");
        assertEquals(foodPairings, response);
    }

    @Test
    public void search() throws Exception {
        mvc.perform(MockMvcRequestBuilders
           .get(url + "/search"))
           .andExpect(MockMvcResultMatchers.status().isOk());
    }
}