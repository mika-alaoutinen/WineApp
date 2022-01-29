package com.mika.WineApp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.mappers.WineMapperImpl;
import com.mika.WineApp.models.wine.Wine;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WebSecurityConfig.class, WineMapperImpl.class, WineCrudController.class})
@WebMvcTest
class WineControllerTest {
    private static final String ENDPOINT = "/wines";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private WineService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void findAll() throws Exception {
        when(service.findAll()).thenReturn(TestData.initWines());

        MvcResult result = mvc
                .perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andReturn();

        var wines = parseResponse(result);
        assertEquals(4, wines.size());
    }

    @Test
    void addWine() throws Exception {
        Wine savedWine = new Wine();
        savedWine.setId(99L);
        when(service.add(any(Wine.class))).thenReturn(savedWine);

        mvc
                .perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWine())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(99L));
    }

    private List<Wine> parseResponse(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String response = TestUtilities.getResponseString(result);
        return objectMapper.readValue(response, new TypeReference<>() {
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
