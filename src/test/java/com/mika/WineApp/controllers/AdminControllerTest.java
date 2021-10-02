package com.mika.WineApp.controllers;

import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = {"ADMIN"})
class AdminControllerTest extends ControllerMvcTest {

    @Test
    void findAll() throws Exception {
        mvc
                .perform(get("/admin/users"))
                .andExpect(status().isOk());

        Mockito
                .verify(userRepository)
                .findAll();
    }

    @Test
    void findById() throws Exception {
        Long userId = 1L;
        Mockito
                .when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User()));

        mvc
                .perform(get("/admin/users/id/{id}", userId))
                .andExpect(status().isOk());

        Mockito
                .verify(userRepository)
                .findById(userId);
    }

    @Test
    void findByUserName() throws Exception {
        String username = "username";
        Mockito
                .when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User()));

        mvc
                .perform(get("/admin/users/username/{username}", username))
                .andExpect(status().isOk());

        Mockito
                .verify(userRepository)
                .findByUsername(username);
    }

    @Test
    void updateRoles() throws Exception {
        Long userId = 1L;
        User newUser = new User("name", "password");
        newUser.setId(userId);
        newUser.setRoles(Set.of(Role.ROLE_ADMIN));

        Mockito
                .when(userRepository.findById(userId))
                .thenReturn(Optional.of(newUser));
        Mockito
                .when(userRepository.save(newUser))
                .thenReturn(newUser);

        mvc
                .perform(
                        put("/admin/users/{id}/roles", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[\"ROLE_ADMIN\"]")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ROLE_ADMIN")));

        Mockito
                .verify(userRepository)
                .save(newUser);
    }

    @WithMockUser(roles = {"USER", "GUEST"})
    @ParameterizedTest
    @ValueSource(strings = {"/admin/users", "/admin/users/id/1", "/admin/users/username/name"})
    void shouldThrowHTTP403ForNonAdmins(String endpoint) throws Exception {
        mvc
                .perform(get(endpoint))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"USER", "GUEST"})
    void shouldThrowHTTP403WhenNonAdminUpdates() throws Exception {
        mvc
                .perform(put("/admin/users/1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"ROLE_ADMIN\"]"))
                .andExpect(status().isForbidden());
    }
}
