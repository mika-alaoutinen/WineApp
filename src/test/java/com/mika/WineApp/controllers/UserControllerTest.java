package com.mika.WineApp.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
public class UserControllerTest extends ControllerMvcTest {

    @Test
    void getUsername() throws Exception {
        mvc
                .perform(get("/users/username"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test_user")));

        Mockito
                .verify(securityUtilities)
                .getUsernameFromSecurityContext();
    }

    @Test
    void isLoggedIn() throws Exception {
        mvc
                .perform(get("/users/loggedIn"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("true")));

        Mockito
                .verify(securityUtilities)
                .getUsernameFromSecurityContext();
    }
}
