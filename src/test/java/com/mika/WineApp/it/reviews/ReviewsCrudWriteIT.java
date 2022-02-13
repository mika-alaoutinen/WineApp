package com.mika.WineApp.it.reviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.entities.User;
import com.mika.WineApp.it.IntegrationTestWrite;
import com.mika.WineApp.models.UserPrincipal;
import com.mika.model.NewReviewDTO;
import com.mika.model.ReviewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestWrite
class ReviewsCrudWriteIT {
    private static final String ENDPOINT = "/reviews";
    private static final ObjectMapper MAPPER = TestUtilities.getObjectMapper();
    private static final UserPrincipal USER = new UserPrincipal(new User("test_user", "password"));

    @Autowired
    private MockMvc mvc;

    @Test
    void addReview() throws Exception {
        NewReviewDTO newReview = new NewReviewDTO()
                .author("Mika")
                .date(LocalDate.of(2022, 1, 1))
                .reviewText("Very tasty")
                .rating(4.5);

        mvc
                .perform(post(ENDPOINT + "/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(newReview))
                        .with(user(USER)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").value("Mika"))
                .andExpect(jsonPath("$.date").value("2022-01-01"))
                .andExpect(jsonPath("$.reviewText").value("Very tasty"))
                .andExpect(jsonPath("$.rating").value(4.5));
    }

    @Test
    void editReview() throws Exception {
        long id = 7L;

        MvcResult result = mvc
                .perform(get(ENDPOINT + "/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        ReviewDTO existingReview = MAPPER.readValue(TestUtilities.getResponseString(result), ReviewDTO.class);
        NewReviewDTO editedReview = new NewReviewDTO()
                .author("Edited")
                .date(existingReview.getDate())
                .reviewText("Edited")
                .rating(existingReview.getRating());

        mvc
                .perform(put(ENDPOINT + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(editedReview))
                        .with(user(USER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Edited"))
                .andExpect(jsonPath("$.reviewText").value("Edited"))
                .andExpect(jsonPath("$.date").value(existingReview
                        .getDate()
                        .toString()));
    }

    @Test
    void deleteReview() throws Exception {
        long id = 7L;

        mvc
                .perform(delete(ENDPOINT + "/{id}", id).with(user(USER)))
                .andExpect(status().isNoContent());

        mvc
                .perform(get(ENDPOINT + "/{id}", id))
                .andExpect(status().isNotFound());
    }
}
