package com.mika.WineApp.it.wines;

import com.mika.WineApp.it.IntegrationTestRead;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
class WinesSearchIT {
    private static final String ENDPOINT = "/wines/search";

    @Autowired
    private MockMvc mvc;

    @Test
    void searchWinesByName() throws Exception {
        mvc
                .perform(get(ENDPOINT + "?name=Valkoviini"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Valkoviini 1"))
                .andExpect(jsonPath("$[1].name").value("Valkoviini 2"));
    }

    @Test
    void searchWinesByType() throws Exception {
        mvc
                .perform(get(ENDPOINT + "?type=RED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].type").value("RED"))
                .andExpect(jsonPath("$[1].type").value("RED"));
    }

    @Test
    void searchWinesByCountry() throws Exception {
        mvc
                .perform(get(ENDPOINT + "?countries=Espanja"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].country").value("Espanja"))
                .andExpect(jsonPath("$[1].country").value("Espanja"));
    }

    @Test
    void searchWinesByVolume() throws Exception {
        mvc
                .perform(get(ENDPOINT + "?volumes=0.75,1.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].volume").value(0.75))
                .andExpect(jsonPath("$[1].volume").value(0.75));
    }

    @Test
    void searchWinesByPriceRange() throws Exception {
        mvc
                .perform(get(ENDPOINT + "?priceRange=28,30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].price").value(29.95));
    }
}
