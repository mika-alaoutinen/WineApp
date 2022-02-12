package com.mika.WineApp.security;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.Role;
import com.mika.WineApp.models.User;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.security.model.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityUtilitiesTest {
    private static final Wine wine = TestData
            .initWines()
            .get(0);
    private static final List<User> testUsers = TestData.initTestUsers();
    private static final User user = testUsers.get(0);
    private static final User admin = testUsers.get(1);
    private UserPrincipal userPrincipal;

    private final SecurityUtilitiesImpl securityUtils = new SecurityUtilitiesImpl();

    @BeforeEach
    void setupMocks() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        UserPrincipal mockUserPrincipal = mock(UserPrincipal.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .thenReturn(mockUserPrincipal);

        userPrincipal = mockUserPrincipal;
    }

    @Test
    void getUsernameFromSecurityContext() {
        when(userPrincipal.getUsername()).thenReturn(user.getUsername());
        String name = securityUtils.getUsernameFromSecurityContext();
        assertEquals(user.getUsername(), name);
    }

    @Test
    void validateUpdateRequestForOwner() {
        wine.setUser(user);
        when(userPrincipal.getUsername()).thenReturn(user.getUsername());
        securityUtils.isUserAllowedToEdit(wine, user);
    }

    @Test
    void shouldAllowAdminsToEdit() {
        wine.setUser(user);
        when(userPrincipal.getUsername()).thenReturn(admin.getUsername());
        assertTrue(securityUtils.isUserAllowedToEdit(wine, admin));
    }

    @Test
    void shouldAllowOwnerToEdit() {
        wine.setUser(user);
        when(userPrincipal.getUsername()).thenReturn(user.getUsername());
        assertTrue(securityUtils.isUserAllowedToEdit(wine, user));
    }

    @Test
    void shouldNotAllowOtherUsersToEdit() {
        User nonOwner = new User();
        nonOwner.setId(123L);
        nonOwner.setRoles(Set.of(Role.ROLE_USER));
        wine.setUser(user);

        when(userPrincipal.getUsername()).thenReturn(nonOwner.getUsername());
        assertFalse(securityUtils.isUserAllowedToEdit(wine, user));
    }

    @Test
    void shouldHandleGetUserPrincipalWhenUserNotLoggedIn() {
        when(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .thenReturn("anonymousUser");

        String usernameOfLoggedInUser = securityUtils.getUsernameFromSecurityContext();
        assertNull(usernameOfLoggedInUser);
    }
}
