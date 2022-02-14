package com.mika.WineApp.authentication;

import com.mika.WineApp.entities.EntityModel;
import com.mika.WineApp.entities.User;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.infra.security.SecurityUtilities;
import com.mika.WineApp.users.UserReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationTest {
    private static final String USERNAME = "user";

    @Mock
    private UserReader reader;

    @Mock
    private SecurityUtilities security;

    @InjectMocks
    private UserAuth utils;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void userShouldBeAllowedToEditWhenOwner(boolean allowedToEdit) {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.of(USERNAME));
        when(reader.findByUsername(USERNAME)).thenReturn(Optional.of(new User(USERNAME, "password")));
        when(security.isUserAllowedToEdit(any(EntityModel.class), any(User.class))).thenReturn(allowedToEdit);

        assertEquals(allowedToEdit, utils.isUserAllowedToEdit(new Wine()));
        verify(security, atLeastOnce()).getUsernameFromSecurityContext();
        verify(reader, atLeastOnce()).findByUsername(USERNAME);
        verify(security, atLeastOnce()).isUserAllowedToEdit(any(EntityModel.class), any(User.class));
    }

    @Test
    void setsLoggedInUserToEntity() {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.of(USERNAME));
        when(reader.findByUsername(USERNAME)).thenReturn(Optional.of(new User(USERNAME, "password")));

        EntityModel entityWithUser = utils.setUser(new Wine());
        assertEquals(USERNAME, entityWithUser
                .getUser()
                .getUsername());
    }

    @Test
    void doesNothingIfNoUserLoggedIn() {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.empty());
        EntityModel entityWithoutUser = utils.setUser(new Wine());
        assertNull(entityWithoutUser.getUser());
    }

    // This should never happen
    @Test
    void doesNothingIfUserNotFoundInRepository() {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.of(USERNAME));
        when(reader.findByUsername(USERNAME)).thenReturn(Optional.empty());
        EntityModel entityWithoutUser = utils.setUser(new Wine());
        assertNull(entityWithoutUser.getUser());
    }
}
