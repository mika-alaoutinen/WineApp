package com.mika.WineApp.mappers;

import com.mika.WineApp.models.user.Role;
import com.mika.model.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleMapperTest {
    private static final RoleMapperImpl MAPPER = new RoleMapperImpl();

    @Test
    void guestToDTO() {
        assertEquals(UserDTO.RolesEnum.GUEST, MAPPER.toDTO(Role.ROLE_GUEST));
    }

    @Test
    void userToDTO() {
        assertEquals(UserDTO.RolesEnum.USER, MAPPER.toDTO(Role.ROLE_USER));
    }

    @Test
    void adminToDTO() {
        assertEquals(UserDTO.RolesEnum.ADMIN, MAPPER.toDTO(Role.ROLE_ADMIN));
    }

    @Test
    void guestToRole() {
        assertEquals(Role.ROLE_GUEST, MAPPER.toModel(UserDTO.RolesEnum.GUEST));
    }

    @Test
    void userToRole() {
        assertEquals(Role.ROLE_USER, MAPPER.toModel(UserDTO.RolesEnum.USER));
    }

    @Test
    void adminToRole() {
        assertEquals(Role.ROLE_ADMIN, MAPPER.toModel(UserDTO.RolesEnum.ADMIN));
    }
}
