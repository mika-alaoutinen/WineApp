package com.mika.WineApp.it.admin;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestRead;
import com.mika.WineApp.users.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTestRead
@WithMockUser(roles = {"ADMIN"})
class AdminControllerIT {
    private static final String ENDPOINT = "/admin/users";

    @Autowired
    UserRepository repository;

    @Autowired
    MockMvc mvc;

    @BeforeAll
    void setupRepository() {
        repository.saveAll(TestData.initTestUsers());
    }

    @Test
    void findAll() throws Exception {
        mvc
                .perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void findById() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test_user"));
    }

    @Test
    void findByUserName() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/username/test_admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test_admin"));
    }

    @ParameterizedTest
    @ValueSource(strings = {ENDPOINT, ENDPOINT + "/id/1", ENDPOINT + "/username/name"})
    @WithMockUser(roles = {"USER", "GUEST"})
    void shouldThrowHTTP403ForNonAdmins(String endpoint) throws Exception {
        mvc
                .perform(get(endpoint))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"USER", "GUEST"})
    void shouldThrowHTTP403WhenNonAdminUpdates() throws Exception {
        mvc
                .perform(put(ENDPOINT + "/1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"ROLE_ADMIN\"]"))
                .andExpect(status().isForbidden());
    }
}
