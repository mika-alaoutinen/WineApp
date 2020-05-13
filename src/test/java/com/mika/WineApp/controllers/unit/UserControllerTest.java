package com.mika.WineApp.controllers.unit;

import com.mika.WineApp.controllers.UserController;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private static final Long id = 1L;

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Test
    public void getUsername() {
        controller.getUsername();
        verify(service, times(1)).getUsername();
    }

    @Test
    public void findAll() {
        controller.findAll();
        verify(service, times(1)).findAll();
    }

    @Test
    public void findById() {
        controller.findById(id);
        verify(service, times(1)).findById(id);
    }

    @Test
    public void findByUsername() {
        controller.findByUsername("username");
        verify(service, times(1)).findByUserName("username");
    }

    @Test
    public void updateRoles() {
        var roles = Set.of(Role.ROLE_ADMIN);
        controller.updateRoles(id, roles);
        verify(service, times(1)).updateRoles(id, roles);
    }
}
