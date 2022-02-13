package com.mika.WineApp.it.authentication;

import com.mika.WineApp.it.IntegrationTestWrite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTestWrite
class AuthenticationControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    void loginShouldReturnToken() throws Exception {
        String userJson = """
                {
                    "username": "test_user",
                    "password": "test_user_password"
                }
                """;

        mvc
                .perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bearer")));
    }

    @Test
    void registerShouldSaveNewUser() throws Exception {
        String newUserJson = """
                {
                    "username": "new_user",
                    "password": "new_password"
                }
                """;

        mvc
                .perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("new_user"))
                .andExpect(jsonPath("$.roles", contains("ROLE_USER")));
    }
}