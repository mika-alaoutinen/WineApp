package com.mika.WineApp.authentication;

import com.mika.WineApp.infra.security.SecurityUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private SecurityUtilities security;

    @InjectMocks
    private UserInfoServiceImpl service;

    @Test
    void shouldGetUsernameFromSecurityContext() {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.of("test_user"));
        assertEquals("test_user", service.getUsername());
    }

    @Test
    void shouldReturnEmptyStringIfNoLoggedInUser() {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.empty());
        assertTrue(service
                .getUsername()
                .isEmpty());
    }

    @Test
    void usernameIsEmptyIfNoUserInSecurityContext() {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.empty());
        assertTrue(service
                .getUsername()
                .isEmpty());
    }

    @Test
    void isTrueUserInSecurityContext() {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.of("test_user"));
        assertTrue(service.isLoggedIn());
    }

    @Test
    void isFalseWhenNoUserInSecurityContext() {
        when(security.getUsernameFromSecurityContext()).thenReturn(Optional.empty());
        assertFalse(service.isLoggedIn());
    }
}
