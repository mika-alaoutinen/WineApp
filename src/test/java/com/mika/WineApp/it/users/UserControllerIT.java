package com.mika.WineApp.it.users;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails("test_user")
class UserControllerIT extends UserTest {
    private static final String ENDPOINT = "/users";

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
