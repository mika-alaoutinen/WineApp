package com.mika.WineApp.it.reviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.it.IntegrationTestWrite;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.reviews.ReviewRepository;
import com.mika.WineApp.security.model.UserPrincipal;
import com.mika.WineApp.wines.WineRepository;
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
    private static final ObjectMapper MAPPER = TestUtilities.getObjectMapper();
    private static final UserPrincipal USER = new UserPrincipal(new User("test_user", "password"));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setupRepositories() {
        userRepository.saveAll(TestData.initTestUsers());

        // Need to save wine to repository, because otherwise reviews would have an unsaved transient dependency
        var wine = TestData
                .initWines()
                .get(0);
        wineRepository.save(wine);

        var reviews = TestData.initReviews();
        reviews.forEach(r -> r.setWine(wine));
        reviewRepository.saveAll(reviews);
    }

    @Test
    void addReview() throws Exception {
        NewReviewDTO newReview = new NewReviewDTO()
                .author("Mika")
                .date(LocalDate.of(2022, 1, 1))
                .reviewText("Very tasty")
                .rating(4.5);

        mvc
                .perform(post(ENDPOINT + "/1")
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
                .perform(get(ENDPOINT + "/2"))
                .andExpect(status().isOk())
                .andReturn();

        ReviewDTO existingReview = MAPPER.readValue(TestUtilities.getResponseString(result), ReviewDTO.class);
        NewReviewDTO editedReview = new NewReviewDTO()
                .author("Edited")
                .date(existingReview.getDate())
                .reviewText("Edited")
                .rating(existingReview.getRating());

        mvc
                .perform(put(ENDPOINT + "/2")
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
                .perform(delete(ENDPOINT + "/2").with(user(USER)))
                .andExpect(status().isNoContent());

        assertFalse(reviewRepository.existsById(2L));
    }
}
