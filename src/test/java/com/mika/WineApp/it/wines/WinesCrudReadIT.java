package com.mika.WineApp.it.wines;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.UserPrincipal;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    void isWineEditable() throws Exception {
        UserPrincipal user = new UserPrincipal(new User("test_user", "password"));

        mvc
                .perform(get(ENDPOINT + "/1/editable").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
