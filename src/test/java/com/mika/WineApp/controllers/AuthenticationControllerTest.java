package com.mika.WineApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.WineApp.TestUtilities.TestUtilities;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.security.model.JwtToken;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    private static final String USERNAME = "test_user";
    private static final String PASSWORD = "test_user_password";
    private static final String USER_JSON = String.format("""
            {
                "username": "%1$s",
                "password": "%2$s"
            }
            """, USERNAME, PASSWORD);

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private UserRepository repository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginShouldReturnToken() throws Exception {
        UserPrincipal principal = new UserPrincipal(1L, USERNAME, PASSWORD, List.of());
        Object credentials = new Object();

        Mockito
                .when(authManager.authenticate(any(Authentication.class)))
                .thenReturn(new TestingAuthenticationToken(principal, credentials));

        String response = doPost("/auth/login");
        JwtToken token = objectMapper.readValue(response, JwtToken.class);

        assertFalse(token
                .getToken()
                .isBlank());

        assertEquals("Bearer", token.getType());

        Mockito
                .verify(authManager)
                .authenticate(any(Authentication.class));
    }

    @Test
    void registerShouldSaveNewUser() throws Exception {
        User savedUser = new User(USERNAME, PASSWORD);
        savedUser.setId(1L);

        Mockito
                .when(repository.save(any(User.class)))
                .thenReturn(savedUser);

        String response = doPost("/auth/register");
        User newUser = objectMapper.readValue(response, User.class);

        assertEquals(1L, newUser.getId());
        assertEquals(USERNAME, newUser.getUsername());
        assertEquals(Set.of(Role.ROLE_USER), newUser.getRoles());
        Mockito
                .verify(repository)
                .save(any(User.class));
    }

    private String doPost(String url) throws Exception {
        MvcResult result = mvc
                .perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(USER_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return TestUtilities.getResponseString(result);
    }
}