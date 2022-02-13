package com.mika.WineApp.it.authentication;

import com.mika.WineApp.it.IntegrationTestRead;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
@WithUserDetails("test_user")
class UserInfoControllerIT {
    private static final String ENDPOINT = "/users";

    @Autowired
    private MockMvc mvc;

    @Test
    void getUsername() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/username"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test_user")));
    }

    @Test
    void isLoggedIn() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/loggedIn"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("true")));
    }
}
