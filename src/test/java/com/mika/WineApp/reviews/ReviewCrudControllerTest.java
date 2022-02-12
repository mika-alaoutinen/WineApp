package com.mika.WineApp.reviews;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.WebSecurityConfig;
import com.mika.WineApp.mappers.ReviewMapperImpl;
import com.mika.WineApp.mappers.WineMapperImpl;
import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.services.ReviewService;
import com.mika.model.NewReviewDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
        WebSecurityConfig.class,
        ReviewMapperImpl.class,
        WineMapperImpl.class,
        ReviewCrudController.class
})
@WebMvcTest
class ReviewCrudControllerTest {
    private static final String ENDPOINT = "/reviews";
    private static final ObjectMapper MAPPER = TestUtilities.getObjectMapper();

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
    void findAllReturnsListOfReviews() throws Exception {
        when(service.findAll()).thenReturn(List.of(REVIEW));
        mvc
                .perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Author"))
                .andExpect(jsonPath("$[0].date").value("2022-01-01"))
                .andExpect(jsonPath("$[0].reviewText").value("Review text"))
                .andExpect(jsonPath("$[0].rating").value(5.0));
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
    void findByIdReturnsOneReview() throws Exception {
        when(service.findById(anyLong())).thenReturn(Optional.of(REVIEW));
        mvc
                .perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.date").value("2022-01-01"))
                .andExpect(jsonPath("$.reviewText").value("Review text"))
                .andExpect(jsonPath("$.rating").value(5.0));
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
    void addReview() throws Exception {
        Review savedReview = Review
                .builder()
                .id(99L)
                .build();

        when(service.add(anyLong(), any(Review.class))).thenReturn(Optional.of(savedReview));

        mvc
                .perform(post(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(99L));
    }

    @Test
    void addReviewReturns400() throws Exception {
        mvc
                .perform(post(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidPayload()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addReviewReturns404() throws Exception {
        when(service.edit(anyLong(), any(Review.class))).thenReturn(Optional.empty());
        mvc
                .perform(post(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload()))
                .andExpect(status().isNotFound());
    }

    @Test
    void editReview() throws Exception {
        when(service.edit(anyLong(), any(Review.class))).thenReturn(Optional.of(REVIEW));
        mvc
                .perform(put(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.date").value("2022-01-01"))
                .andExpect(jsonPath("$.reviewText").value("Review text"))
                .andExpect(jsonPath("$.rating").value(5.0));
    }

    @Test
    void editReviewReturns400() throws Exception {
        mvc
                .perform(put(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidPayload()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editReviewReturns404() throws Exception {
        when(service.edit(anyLong(), any(Review.class))).thenReturn(Optional.empty());
        mvc
                .perform(put(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload()))
                .andExpect(status().isNotFound());
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

    @Test
    void deleteReview() throws Exception {
        mvc
                .perform(delete(ENDPOINT + "/1"))
                .andExpect(status().isNoContent());
    }

    private static String createPayload() throws JsonProcessingException {
        return MAPPER.writeValueAsString(new NewReviewDTO()
                .author("New author")
                .date(LocalDate.of(2022, 1, 1))
                .reviewText("New review")
                .rating(1.0));
    }

    private static String createInvalidPayload() throws JsonProcessingException {
        return MAPPER.writeValueAsString(new NewReviewDTO()
                .author("New author")
                .date(LocalDate.of(2022, 1, 1))
                .reviewText("New review")
                .rating(-1.0));
    }
}
