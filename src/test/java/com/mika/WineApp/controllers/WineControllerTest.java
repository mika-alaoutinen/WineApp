package com.mika.WineApp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.mappers.WineMapperImpl;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.models.wine.WineType;
import com.mika.WineApp.services.WineService;
import com.mika.model.WineDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WebSecurityConfig.class, WineMapperImpl.class, WineCrudController.class})
@WebMvcTest
class WineControllerTest {
    private static final String ENDPOINT = "/wines";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Wine WINE = Wine
            .builder()
            .name("Name")
            .type(WineType.WHITE)
            .country("Country")
            .price(8.75)
            .volume(0.75)
            .description(List.of("description"))
            .foodPairings(List.of("food pairing"))
            .url("url")
            .reviews(Collections.emptyList())
            .build();

    private static final WineDTO DTO = new WineDTO()
            .name("Name")
            .type(WineDTO.TypeEnum.WHITE)
            .country("Country")
            .price(8.75)
            .volume(0.75)
            .description(List.of("description"))
            .foodPairings(List.of("food pairing"))
            .url("url");

    @MockBean
    private WineService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void findAllReturnsListOfWines() throws Exception {
        when(service.findAll()).thenReturn(List.of(WINE));
        mvc
                .perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(DTO));
    }

    @Test
    void findAllReturnsEmptyList() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());
        mvc
                .perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void findByIdReturnsOneWine() throws Exception {
        when(service.findById(anyLong())).thenReturn(Optional.of(WINE));
        mvc
                .perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(DTO));
    }

    @Test
    void findByIdReturns404WithNoBody() throws Exception {
        when(service.findById(anyLong())).thenReturn(Optional.empty());
        mvc
                .perform(get(ENDPOINT + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void addWine() throws Exception {
        Wine savedWine = new Wine();
        savedWine.setId(99L);
        when(service.add(any(Wine.class))).thenReturn(savedWine);

        mvc
                .perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(createWine())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(99L));
    }

    private List<WineDTO> parseResponse(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String response = TestUtilities.getResponseString(result);
        return MAPPER.readValue(response, new TypeReference<>() {
        });
    }

    private static WineDTO createWine() {
        return new WineDTO()
                .id(99L)
                .name("New Wine")
                .type(WineDTO.TypeEnum.RED)
                .country("France")
                .price(12.50)
                .volume(0.75)
                .description(List.of("delicious"))
                .foodPairings(List.of("goes with everything"));
    }
}
