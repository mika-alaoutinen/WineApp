package com.mika.WineApp.security;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.security.model.UserPrincipal;
import com.mika.WineApp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class SecurityUtilitiesTest {
    private static final Wine wine = TestData.initWines().get(0);
    private static final List<User> testUsers = TestData.initTestUsers();
    private static final User user = testUsers.get(0);
    private static final User admin = testUsers.get(1);
    private UserPrincipal userPrincipal;

    @Mock
    private UserService service;

    @InjectMocks
    private SecurityUtilitiesImpl securityUtils;

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
        Mockito.when(service.findById(user.getId())).thenReturn(user);

        securityUtils.validateUpdateRequest(wine);
    }

    @Test
    public void validateUpdateRequestForAdmin() {
        wine.setUser(user);
        System.out.println("id " + admin.getId());

        Mockito.when(userPrincipal.getId()).thenReturn(admin.getId());
        Mockito.when(service.findById(admin.getId())).thenReturn(admin);

        securityUtils.validateUpdateRequest(wine);
    }

    @Test
    public void shouldThrowExceptionOnFailedValidation() {
        wine.setUser(admin);

        Mockito.when(userPrincipal.getId()).thenReturn(user.getId());
        Mockito.when(service.findById(user.getId())).thenReturn(user);

        Exception e = assertThrows(BadRequestException.class, () ->
                securityUtils.validateUpdateRequest(wine));

        assertEquals("Error: tried to modify review or wine that you do not own!", e.getMessage());
    }
}
