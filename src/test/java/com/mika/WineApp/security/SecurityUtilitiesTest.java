package com.mika.WineApp.security;

import com.mika.WineApp.TestUtilities.TestData;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class SecurityUtilitiesTest {
    private static final User user = TestData.createTestUsers().get(0);

    @Mock
    private UserService service;

    @InjectMocks
    private SecurityUtilitiesImpl securityUtils;

    @BeforeEach
    public void setupMocks() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);

        Mockito.when(securityContext.getAuthentication())
               .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
               .thenReturn(userPrincipal);

        Mockito.lenient()
                .when(userPrincipal.getUsername())
                .thenReturn(user.getUsername());

        Mockito.lenient()
                .when(userPrincipal.getId())
                .thenReturn(user.getId());
    }

    @Test
    public void getUsernameFromSecurityContext() {
        String name = securityUtils.getUsernameFromSecurityContext();
        assertEquals(user.getUsername(), name);
    }

    @Test
    public void validateUpdateRequest() {
        Wine wine = TestData.initWines().get(0);
        wine.setUser(user);

        Mockito.when(service.findById(user.getId()))
               .thenReturn(user);

        securityUtils.validateUpdateRequest(wine);
    }
}
