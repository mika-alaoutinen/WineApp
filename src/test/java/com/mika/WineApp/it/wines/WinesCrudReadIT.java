package com.mika.WineApp.it.wines;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTest;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.security.model.UserPrincipal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests that only read the DB. The DB is populated once before running the tests, and it's state
 * persists between tests.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WinesCrudReadIT {
    private static final String ENDPOINT = "/wines";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    void setupRepositories() {
        System.out.println("before " + wineRepository.count());
        userRepository.saveAll(TestData.initTestUsers());
        wineRepository.saveAll(TestData.initWines());
    }

    @Test
    void getAllWines() throws Exception {
        mvc
                .perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4));
    }

    @Test
    void findWineById() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Valkoviini 1"));
    }

    @Test
    void isWineEditable() throws Exception {
        UserPrincipal user = new UserPrincipal(new User("test_user", "password"));

        mvc
                .perform(get(ENDPOINT + "/1/editable").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
