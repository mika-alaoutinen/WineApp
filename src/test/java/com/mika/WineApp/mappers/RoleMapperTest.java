package com.mika.WineApp.mappers;

import com.mika.WineApp.models.Role;
import com.mika.model.RoleDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleMapperTest {
    private static final RoleMapperImpl MAPPER = new RoleMapperImpl();

    @Test
    void guestToDTO() {
        assertEquals(RoleDTO.GUEST, MAPPER.toDTO(Role.ROLE_GUEST));
    }

    @Test
    void userToDTO() {
        assertEquals(RoleDTO.USER, MAPPER.toDTO(Role.ROLE_USER));
    }

    @Test
    void adminToDTO() {
        assertEquals(RoleDTO.ADMIN, MAPPER.toDTO(Role.ROLE_ADMIN));
    }

    @Test
    void guestToRole() {
        assertEquals(Role.ROLE_GUEST, MAPPER.toModel(RoleDTO.GUEST));
    }

    @Test
    void userToRole() {
        assertEquals(Role.ROLE_USER, MAPPER.toModel(RoleDTO.USER));
    }

    @Test
    void adminToRole() {
        assertEquals(Role.ROLE_ADMIN, MAPPER.toModel(RoleDTO.ADMIN));
    }
}
