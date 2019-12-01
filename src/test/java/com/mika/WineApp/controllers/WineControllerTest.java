package com.mika.WineApp.controllers;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WineController.class)
class WineControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WineServiceImpl service;

    @MockBean
    private WineRepository repository;

    private static final String url = "/wines";
    private List<Wine> wineList;

    @BeforeEach
    void setUp() {
        var description1 = List.of("puolikuiva", "sitruunainen", "yrttinen");
        var description2 = List.of("Kuiva", "sitruunainen", "pirskahteleva");
        var description3 = List.of("tanniininen", "mokkainen", "täyteläinen", "tamminen");

        var foodPairings1 = List.of("kala", "kasvisruoka", "seurustelujuoma");
        var foodPairings2 = List.of("kana", "kasvisruoka", "noutopöytä");
        var foodPairings3 = List.of("nauta", "pataruuat", "noutopöytä");

        Wine white1 = new Wine("Valkoviini 1", WineType.WHITE, "Espanja", 8.75, 0.75, description1, foodPairings1, "invalid");
        Wine white2 = new Wine("Valkoviini 2", WineType.WHITE, "Espanja", 8.75, 0.75, description2, foodPairings2, "invalid");
        Wine red1 = new Wine("Punaviini", WineType.RED, "Ranska", 29.95, 3.0, description3, foodPairings3, "invalid");
        Wine red2 = new Wine("Gato Negra", WineType.RED, "Italia", 30.95, 3.0, description3, foodPairings3, "invalid");

        wineList = List.of(white1, white2, red1, red2);
    }

    @Test
    void findAll() throws Exception {
        mvc.perform(get(url)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
}