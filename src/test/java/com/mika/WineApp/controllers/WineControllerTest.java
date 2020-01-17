package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(WineController.class)
class WineControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WineService service;

    @MockBean
    private WineRepository repository;

    private static final String url = "/wines";
    private List<Wine> wines;

    @BeforeEach
    void setUp() {
        var description1 = List.of("puolikuiva", "sitruunainen", "yrttinen");
        var description2 = List.of("kuiva", "sitruunainen", "pirskahteleva");
        var description3 = List.of("tanniininen", "mokkainen", "täyteläinen", "tamminen");

        var foodPairings1 = List.of("kala", "kasvisruoka", "seurustelujuoma");
        var foodPairings2 = List.of("kana", "kasvisruoka", "noutopöytä");
        var foodPairings3 = List.of("nauta", "pataruuat", "noutopöytä");

        var white1 = new Wine("Valkoviini 1", WineType.WHITE, "Espanja", 8.75, 0.75, description1, foodPairings1, "invalid");
        white1.setId(1L);

        var white2 = new Wine("Valkoviini 2", WineType.WHITE, "Espanja", 8.75, 0.75, description2, foodPairings2, "invalid");
        white2.setId(2L);

        var red1 = new Wine("Punaviini", WineType.RED, "Ranska", 29.95, 3.0, description3, foodPairings3, "invalid");
        red1.setId(3L);

        var red2 = new Wine("Gato Negra", WineType.RED, "Italia", 30.95, 3.0, description3, foodPairings3, "invalid");
        red2.setId(4L);

        wines = List.of(white1, white2, red1, red2);
    }

    @Test
    public void findAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addWine() throws Exception {
        Wine wine = wines.get(0);

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
    public void getDescriptions() throws Exception {
        var descriptions = List.of("puolikuiva", "sitruunainen", "yrttinen");

        Mockito.when(repository.findAllDescriptions()).thenReturn(descriptions);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url + "/descriptions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var words = parseWordsFromResponse(response);

        Assertions.assertEquals(descriptions, words);
    }

    /**
     * Parses response string into a list of strings.
     * The response string is scrubbed of brackets and quotation marks.
     * @param response given by controller.
     * @return List of words.
     */
    private List<String> parseWordsFromResponse(String response) {
        String[] words = response
                .substring(1, response.length() - 1)
                .replace("\"", "")
                .split(",");

        return List.of(words);
    }
}