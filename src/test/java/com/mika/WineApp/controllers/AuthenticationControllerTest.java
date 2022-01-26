package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestConfig;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.security.model.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
class AuthenticationControllerTest {

    private static final String USERNAME = "test_user";
    private static final String PASSWORD = "password";
    private static final String USER_JSON = String.format("""
            {
                "username": "%1$s",
                "password": "%2$s"
            }
            """, USERNAME, PASSWORD);

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    protected UserRepository userRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mvc;

    @Test
    void loginShouldReturnToken() throws Exception {
        UserPrincipal principal = new UserPrincipal(1L, USERNAME, PASSWORD, List.of());
        Object credentials = new Object();

        Mockito
                .when(authManager.authenticate(any(Authentication.class)))
                .thenReturn(new TestingAuthenticationToken(principal, credentials));

        mvc
                .perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(USER_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bearer")));

        Mockito
                .verify(authManager)
                .authenticate(any(Authentication.class));
    }

    @Test
    void registerShouldSaveNewUser() throws Exception {
        User savedUser = new User(USERNAME, PASSWORD);
        savedUser.setId(1L);

        Mockito
                .when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        MvcResult result = mvc
                .perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(USER_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        User newUser = objectMapper.readValue(response, User.class);

        assertEquals(1L, newUser.getId());
        assertEquals(USERNAME, newUser.getUsername());
        assertEquals(Set.of(Role.ROLE_USER), newUser.getRoles());
        Mockito
                .verify(userRepository)
                .save(any(User.class));
    }
}