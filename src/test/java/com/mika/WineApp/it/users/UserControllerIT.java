package com.mika.WineApp.it.users;

import com.mika.WineApp.it.IntegrationTest;
import com.mika.WineApp.security.SecurityUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WithMockUser
class UserControllerIT {
    private static final String ENDPOINT = "/users";

    @MockBean
    private SecurityUtilities securityUtilities;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setupSecurityUtils() {
        when(securityUtilities.getUsernameFromSecurityContext()).thenReturn("test_user");
    }

    @Test
    void getUsername() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/username"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test_user")));

        verify(securityUtilities).getUsernameFromSecurityContext();
    }

    @Test
    void isLoggedIn() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/loggedIn"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("true")));

        verify(securityUtilities).getUsernameFromSecurityContext();
    }
}
