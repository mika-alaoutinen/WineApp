package com.mika.WineApp.it;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

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

    @AfterAll
    void resetRepositories() {
        System.out.println("after " + wineRepository.count());
        wineRepository.deleteAll();
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
}
