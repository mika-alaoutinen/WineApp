package com.mika.WineApp.it.wines;

import com.mika.WineApp.it.IntegrationTestRead;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
class WinesCrudReadIT {
    private static final String ENDPOINT = "/wines";

    @Autowired
    private MockMvc mvc;

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
                .perform(get(ENDPOINT + "/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Valkoviini 1"));
    }

    @Test
    @WithUserDetails("test_user")
    void isWineEditable() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/3/editable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
