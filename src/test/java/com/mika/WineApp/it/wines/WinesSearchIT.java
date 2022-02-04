package com.mika.WineApp.it.wines;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestRead;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
class WinesSearchIT {
    private static final String ENDPOINT = "/wines";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    void setupRepository() {
        userRepository.saveAll(TestData.initTestUsers());
        wineRepository.saveAll(TestData.initWines());
    }

    @Test
    void searchWinesByName() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/search?name=Valkoviini 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Valkoviini 1"));
    }
}
