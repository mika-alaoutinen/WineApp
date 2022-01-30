package com.mika.WineApp.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.security.model.UserPrincipal;
import com.mika.model.WineDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests that write to the DB. The entire Spring context is recreated for each test to ensure
 * that the DB is always in a predictable state.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@IntegrationTest
class WinesCrudWriteIT {
    private static final String ENDPOINT = "/wines";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setupRepositories() {
        userRepository.saveAll(TestData.initTestUsers());
        wineRepository.saveAll(TestData.initWines());
    }

    @Test
    void addWine() throws Exception {
        UserPrincipal user = new UserPrincipal(new User("test_user", "password"));

        mvc
                .perform(
                        post(ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createWine()))
                                .with(user(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("New Wine"));
    }

    private static WineDTO createWine() {
        return new WineDTO()
                .name("New Wine")
                .type(WineDTO.TypeEnum.RED)
                .country("France")
                .price(12.50)
                .volume(0.75)
                .description(List.of("delicious"))
                .foodPairings(List.of("goes with everything"));
    }
}
