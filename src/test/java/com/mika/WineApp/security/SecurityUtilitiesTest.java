package com.mika.WineApp.security;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.security.model.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class SecurityUtilitiesTest {
    private static final Wine wine = TestData.initWines().get(0);
    private static final List<User> testUsers = TestData.initTestUsers();
    private static final User user = testUsers.get(0);
    private static final User admin = testUsers.get(1);
    private UserPrincipal userPrincipal;

    private final SecurityUtilitiesImpl securityUtils = new SecurityUtilitiesImpl();

    @BeforeEach
    public void setupMocks() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        UserPrincipal mockUserPrincipal = mock(UserPrincipal.class);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(securityContext.getAuthentication())
               .thenReturn(authentication);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
               .thenReturn(mockUserPrincipal);

        userPrincipal = mockUserPrincipal;
    }

    @Test
    public void getUsernameFromSecurityContext() {
        Mockito.when(userPrincipal.getUsername()).thenReturn(user.getUsername());

        String name = securityUtils.getUsernameFromSecurityContext();
        assertEquals(user.getUsername(), name);
    }

    @Test
    public void validateUpdateRequestForOwner() {
        wine.setUser(user);
        Mockito.when(userPrincipal.getId()).thenReturn(user.getId());
        securityUtils.isUserAllowedToEdit(wine, user);
    }

    @Test
    public void shouldAllowAdminsToEdit() {
        wine.setUser(user);
        Mockito.when(userPrincipal.getId()).thenReturn(admin.getId());
        assertTrue(securityUtils.isUserAllowedToEdit(wine, admin));
    }

    @Test
    public void shouldAllowOwnerToEdit() {
        wine.setUser(user);
        Mockito.when(userPrincipal.getId()).thenReturn(user.getId());
        assertTrue(securityUtils.isUserAllowedToEdit(wine, user));
    }

    @Test
    public void shouldNotAllowOtherUsersToEdit() {
        User nonOwner = new User();
        nonOwner.setId(123L);
        nonOwner.setRoles(Set.of(Role.ROLE_USER));
        wine.setUser(user);

        Mockito.when(userPrincipal.getId()).thenReturn(nonOwner.getId());
        assertFalse(securityUtils.isUserAllowedToEdit(wine, user));
    }
}
