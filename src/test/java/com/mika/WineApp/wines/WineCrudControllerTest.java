package com.mika.WineApp.wines;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.WebSecurityConfig;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.entities.WineType;
import com.mika.WineApp.mappers.WineMapperImpl;
import com.mika.WineApp.services.WineService;
import com.mika.model.NewWineDTO;
import com.mika.model.WineDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WebSecurityConfig.class, WineMapperImpl.class, WineCrudController.class})
@WebMvcTest
class WineCrudControllerTest {
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
        Wine savedWine = Wine
                .builder()
                .id(99L)
                .build();

        when(service.add(any(Wine.class))).thenReturn(savedWine);

        mvc
                .perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(99L));
    }

    @Test
    void addWineReturns400() throws Exception {
        mvc
                .perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidPayload()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editWine() throws Exception {
        when(service.edit(anyLong(), any(Wine.class))).thenReturn(Optional.of(WINE));
        mvc
                .perform(put(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(DTO));
    }

    @Test
    void editWineReturns400() throws Exception {
        mvc
                .perform(put(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidPayload()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editWineReturns404() throws Exception {
        when(service.edit(anyLong(), any(Wine.class))).thenReturn(Optional.empty());
        mvc
                .perform(put(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWine() throws Exception {
        mvc
                .perform(delete(ENDPOINT + "/1"))
                .andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void verifyUserIsAllowedToEdit(boolean isAllowedToEdit) throws Exception {
        when(service.isAllowedToEdit(anyLong())).thenReturn(isAllowedToEdit);
        mvc
                .perform(get(ENDPOINT + "/1/editable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(isAllowedToEdit));
    }

    private static String createPayload() throws JsonProcessingException {
        return MAPPER.writeValueAsString(new NewWineDTO()
                .name("New Wine")
                .type(NewWineDTO.TypeEnum.RED)
                .country("France")
                .price(12.50)
                .volume(0.75)
                .description(List.of("delicious"))
                .foodPairings(List.of("goes with everything"))
        );
    }

    private static String createInvalidPayload() throws JsonProcessingException {
        return MAPPER.writeValueAsString(new NewWineDTO()
                .name("New Wine")
                .type(NewWineDTO.TypeEnum.RED)
                .country("France")
                .price(-1.00)
                .volume(0.0)
                .description(Collections.emptyList())
                .foodPairings(Collections.emptyList()));
    }
}
