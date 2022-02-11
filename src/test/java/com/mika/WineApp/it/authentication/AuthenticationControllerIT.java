package com.mika.WineApp.it.authentication;

import com.mika.WineApp.it.IntegrationTestWrite;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTestWrite
class AuthenticationControllerIT {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MockMvc mvc;

    @Test
    void loginShouldReturnToken() throws Exception {
        String username = "test_user";
        String password = "test_user_password";

        // Need to encode the password before saving the user to DB.
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        User user = new User(username, encryptedPassword);
        user.setRoles(Set.of(Role.ROLE_USER));
        repository.save(user);

        String userJson = String.format("""
                {
                    "username": "%1$s",
                    "password": "%2$s"
                }
                """, username, password);

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