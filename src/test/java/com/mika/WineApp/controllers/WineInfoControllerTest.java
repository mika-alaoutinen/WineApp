package com.mika.WineApp.controllers;

import com.mika.WineApp.mappers.WineMapperImpl;
import com.mika.WineApp.services.WineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WebSecurityConfig.class, WineMapperImpl.class, WineInfoController.class})
@WebMvcTest
class WineInfoControllerTest {
    private static final String ENDPOINT = "/wines";

    @MockBean
    private WineService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void getWineCount() throws Exception {
        when(service.count()).thenReturn(1);
        mvc
                .perform(get(ENDPOINT + "/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void getDistinctWineCountries() throws Exception {
        when(service.findCountries()).thenReturn(List.of("Spain"));
        mvc
                .perform(get(ENDPOINT + "/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", contains("Spain")));
    }

    @Test
    void getDistinctWineDescriptions() throws Exception {
        when(service.findDescriptions()).thenReturn(List.of("Tasty"));
        mvc
                .perform(get(ENDPOINT + "/descriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", contains("Tasty")));
    }

    @Test
    void getDistinctFoodPairings() throws Exception {
        when(service.findFoodPairings()).thenReturn(List.of("Beef"));
        mvc
                .perform(get(ENDPOINT + "/food-pairings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", contains("Beef")));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void validateThatWineNameIsUnique(boolean isUnique) throws Exception {
        when(service.isValid(Mockito.anyString())).thenReturn(isUnique);
        mvc
                .perform(get(ENDPOINT + "/validate?name=Wine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(isUnique));
    }

    @Test
    void validateRequiresQueryParam() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/validate"))
                .andExpect(status().isBadRequest());
    }
}
