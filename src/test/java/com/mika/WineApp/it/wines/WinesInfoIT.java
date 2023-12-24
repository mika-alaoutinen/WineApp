package com.mika.WineApp.it.wines;

import com.mika.WineApp.it.IntegrationTestRead;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
class WinesInfoIT {
    private static final String ENDPOINT = "/wines";

    @Autowired
    private MockMvc mvc;

    @Test
    void getWineCount() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4));
    }

    @Test
    void getCountries() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$", contains("Espanja", "Italia", "Ranska")));
    }

    @Test
    void getDescriptions() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/descriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(9))
                .andExpect(jsonPath("$", hasItems("puolikuiva", "kuiva", "tanniininen")));
    }

    @Test
    void getFoodPairings() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/food-pairings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(8))
                .andExpect(jsonPath("$", hasItems("kala", "kana", "nauta")));
    }

    @Test
    void doesWineNameAlreadyExist() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/validate?name=Punaviini 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }
}
