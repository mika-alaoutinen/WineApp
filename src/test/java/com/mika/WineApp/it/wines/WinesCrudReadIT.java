package com.mika.WineApp.it.wines;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WinesCrudReadIT extends WineTest {
    private static final String ENDPOINT = "/wines";

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
    @WithUserDetails("test_user")
    void isWineEditable() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/1/editable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
