package com.mika.WineApp.controllers;

import com.mika.WineApp.services.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WebSecurityConfig.class, ReviewInfoController.class})
@WebMvcTest
class ReviewInfoControllerTest {
    private static final String ENDPOINT = "/reviews";

    @MockBean
    private ReviewService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void getReviewCount() throws Exception {
        when(service.count()).thenReturn(1);
        mvc
                .perform(get(ENDPOINT + "/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }
}
