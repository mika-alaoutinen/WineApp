package com.mika.WineApp.it.users;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestWrite;
import com.mika.WineApp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestWrite
class AdminWriteIT {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setupRepository() {
        repository.saveAll(TestData.initTestUsers());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateRoles() throws Exception {
        mvc
                .perform(
                        put("/admin/users/1/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[\"ROLE_ADMIN\"]"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ROLE_ADMIN")));
    }
}
