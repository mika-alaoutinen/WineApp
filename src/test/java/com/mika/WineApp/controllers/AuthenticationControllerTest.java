package com.mika.WineApp.controllers;

import com.mika.WineApp.TestUtilities.TestUtilities;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    private static final String USER = """
            {
                "username": "test_user",
                "password": "test_user_password"
            }
            """;

    @MockBean
    private AuthenticationManager authManager;

    @Autowired
    private MockMvc mvc;

    @Test
    void login() throws Exception {
        UserPrincipal principal = new UserPrincipal(1L, "test_user", "test_user_password", List.of());
        Object credentials = new Object();

        Mockito
                .when(authManager.authenticate(Mockito.any()))
                .thenReturn(new TestingAuthenticationToken(principal, credentials));

        MvcResult result = mvc
                .perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(USER))
                .andExpect(status().isOk())
                .andReturn();

        String response = TestUtilities.getResponseString(result);
        System.out.println("response" + response);
        assertNotNull(response);
    }

    @Test
    void register() {
    }
}