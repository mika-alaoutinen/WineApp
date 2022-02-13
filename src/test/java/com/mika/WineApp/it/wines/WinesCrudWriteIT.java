package com.mika.WineApp.it.wines;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.entities.User;
import com.mika.WineApp.it.IntegrationTestWrite;
import com.mika.WineApp.models.UserPrincipal;
import com.mika.WineApp.wines.WineRepository;
import com.mika.model.WineDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests that write to the DB. The entire Spring context is recreated for each test to ensure
 * that the DB is always in a predictable state.
 */
@IntegrationTestWrite
class WinesCrudWriteIT {
    private static final String ENDPOINT = "/wines";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final UserPrincipal USER = new UserPrincipal(new User("test_user", "password"));

    @Autowired
    private WineRepository repository;

    @Autowired
    private MockMvc mvc;

    @Test
    void addWine() throws Exception {
        WineDTO newWine = new WineDTO()
                .name("New Wine")
                .type(WineDTO.TypeEnum.RED)
                .country("France")
                .price(12.50)
                .volume(0.75)
                .description(List.of("delicious"))
                .foodPairings(List.of("goes with everything"));

        mvc
                .perform(
                        post(ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(newWine))
                                .with(user(USER)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.name").value("New Wine"));
    }

    @Test
    void editWine() throws Exception {
        MvcResult result = mvc
                .perform(get(ENDPOINT + "/3"))
                .andExpect(status().isOk())
                .andReturn();

        WineDTO existingWine = MAPPER.readValue(TestUtilities.getResponseString(result), WineDTO.class);
        existingWine.setName("Edited");
        existingWine.setCountry("Edited");

        mvc
                .perform(put(ENDPOINT + "/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(existingWine))
                        .with(user(USER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Edited"))
                .andExpect(jsonPath("$.country").value("Edited"));

    }

    @Test
    void deleteWine() throws Exception {
        mvc
                .perform(delete(ENDPOINT + "/3").with(user(USER)))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById(1L));
    }
}
