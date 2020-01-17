package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
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

import java.io.UnsupportedEncodingException;
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

    private List<String> countries;
    private List<String> descriptions;
    private List<String> foodPairings;
    private Wine wine;
    private final String url = "/wines";

    public WineControllerTest() {
        this.countries = List.of("Espanja", "Italia", "Ranska");
        this.descriptions = List.of("puolikuiva", "sitruunainen", "yrttinen");
        this.foodPairings = List.of("kana", "kala", "seurustelujuoma");
        this.wine = new Wine("Valkoviini 1", WineType.WHITE, "Espanja", 8.75, 0.75, descriptions, foodPairings, "invalid");
    }

    @Test
    public void findAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addWine() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                    .post(url)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(wine)))
           .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void deleteWine() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", 1L))
           .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void getCountries() throws Exception {
        Mockito.when(repository.findAllCountries()).thenReturn(countries);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/countries"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = parseWordsFromResponse(result);
        Assertions.assertEquals(countries, response);
    }

    @Test
    public void getDescriptions() throws Exception {
        Mockito.when(repository.findAllDescriptions()).thenReturn(descriptions);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/descriptions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = parseWordsFromResponse(result);
        Assertions.assertEquals(descriptions, response);
    }

    @Test
    public void getFoodPairings() throws Exception {
        Mockito.when(repository.findAllFoodPairings()).thenReturn(foodPairings);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/food-pairings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = parseWordsFromResponse(result);
        Assertions.assertEquals(foodPairings, response);
    }

    /**
     * Parses result from controller into a list of strings.
     * The result is turned into a string, which is then scrubbed of brackets and quotation marks.
     * @param result given by controller.
     * @return List of words.
     */
    private List<String> parseWordsFromResponse(MvcResult result) throws UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();

        String[] words = response
                .substring(1, response.length() - 1)
                .replace("\"", "")
                .split(",");

        return List.of(words);
    }
}