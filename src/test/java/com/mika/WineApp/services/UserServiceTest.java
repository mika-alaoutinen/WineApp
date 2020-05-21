package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.security.SecurityUtilities;
import com.mika.WineApp.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final static List<User> users = TestData.initTestUsers();
    private final static User user = users.get(0);
    private final static String username = user.getUsername();
    private final static Wine wine = TestData.initWines().get(0);

    @Mock
    private AdminService adminService;

    @Mock
    private SecurityUtilities securityUtils;

    @InjectMocks
    UserServiceImpl service;

    @BeforeEach
    public void setupMocks() {
        Mockito.lenient()
                .when(securityUtils.getUsernameFromSecurityContext())
                .thenReturn(user.getUsername());

        Mockito.lenient()
                .when(adminService.findByUserName(username))
                .thenReturn(user);
    }

    @Test
    public void getUsername() {
        String username = service.getUsername();
        verify(securityUtils, times(1)).getUsernameFromSecurityContext();
        assertEquals(user.getUsername(), username);
    }

    @Test
    public void isLoggedIn() {
        assertTrue(service.isLoggedIn());
        verify(securityUtils, times(1)).getUsernameFromSecurityContext();
    }

    @Test
    public void notLoggedIn() {
        Mockito.when(securityUtils.getUsernameFromSecurityContext())
               .thenReturn(null);

        assertFalse(service.isLoggedIn());
        verify(securityUtils, times(1)).getUsernameFromSecurityContext();
    }

    @Test
    public void isUserAllowedToEdit() {
        Mockito.when(securityUtils.getUsernameFromSecurityContext())
               .thenReturn(user.getUsername());

        Mockito.when(securityUtils.isUserAllowedToEdit(wine, user))
               .thenReturn(true);

        assertTrue(service.isUserAllowedToEdit(wine));
        verify(securityUtils, times(1)).getUsernameFromSecurityContext();
        verify(adminService, times(1)).findByUserName(username);
        verify(securityUtils, times(1)).isUserAllowedToEdit(wine, user);
    }

    @Test
    public void userIsNotAllowedToEditWhenNotLoggedIn() {
        Mockito.when(securityUtils.getUsernameFromSecurityContext())
               .thenReturn(null);

        assertFalse(service.isUserAllowedToEdit(wine));
        verify(securityUtils, times(1)).getUsernameFromSecurityContext();
        verify(adminService, times(0)).findByUserName(username);
        verify(securityUtils, times(0)).isUserAllowedToEdit(any(EntityModel.class), any(User.class));
    }

    @Test
    public void setUser() {
        EntityModel wineWithUser = service.setUser(wine);
        assertEquals(user, wineWithUser.getUser());
        verify(securityUtils, times(1)).getUsernameFromSecurityContext();
    }
}
