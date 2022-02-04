package com.mika.WineApp.it.reviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.it.IntegrationTestWrite;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.security.model.UserPrincipal;
import com.mika.model.NewReviewDTO;
import com.mika.model.ReviewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestWrite
public class ReviewsCrudWriteIT {
    private static final String ENDPOINT = "/reviews";
    private static final ObjectMapper MAPPER = createMapper();
    private static final UserPrincipal USER = new UserPrincipal(new User("test_user", "password"));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setupRepositories() {
        userRepository.saveAll(TestData.initTestUsers());
        reviewRepository.saveAll(TestData.initReviews());
    }

    @Test
    void addReview() throws Exception {
        NewReviewDTO newReview = new NewReviewDTO()
                .author("Mika")
                .date(LocalDate.of(2022, 1, 1))
                .reviewText("Very tasty")
                .rating(4.5);

        mvc
                .perform(post(ENDPOINT + "/2")
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
        MvcResult result = mvc
                .perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andReturn();

        ReviewDTO existingReview = MAPPER.readValue(TestUtilities.getResponseString(result), ReviewDTO.class);
        NewReviewDTO editedReview = new NewReviewDTO()
                .author("Edited")
                .date(existingReview.getDate())
                .reviewText("Edited")
                .rating(existingReview.getRating());

        mvc
                .perform(put(ENDPOINT + "/1")
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
        mvc
                .perform(delete(ENDPOINT + "/1").with(user(USER)))
                .andExpect(status().isNoContent());

        assertFalse(reviewRepository.existsById(1L));
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Needed to handle LocalDates
        return mapper;
    }
}
