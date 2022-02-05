package com.mika.WineApp.controllers;

import com.mika.WineApp.mappers.ReviewMapperImpl;
import com.mika.WineApp.mappers.WineMapperImpl;
import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.services.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
        WebSecurityConfig.class,
        ReviewMapperImpl.class,
        WineMapperImpl.class,
        ReviewsByWineController.class
})
@WebMvcTest
class ReviewsByWineControllerTest {
    private static final String ENDPOINT = "/reviews/wine";

    private static final Review REVIEW = Review
            .builder()
            .author("Author")
            .date(LocalDate.of(2022, 1, 1))
            .reviewText("Review text")
            .rating(5.0)
            .wine(new Wine())
            .build();

    @MockBean
    private ReviewService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void getReviewsByWineName() throws Exception {
        when(service.findByWineName(anyString())).thenReturn(List.of(REVIEW));
        mvc
                .perform(get(ENDPOINT + "/name/Huttunen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getReviewsByWineId() throws Exception {
        when(service.findByWineId(anyLong())).thenReturn(List.of(REVIEW));
        mvc
                .perform(get(ENDPOINT + "/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
