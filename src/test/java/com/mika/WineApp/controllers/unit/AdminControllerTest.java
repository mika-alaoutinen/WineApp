package com.mika.WineApp.controllers.unit;

import com.mika.WineApp.controllers.AdminController;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.services.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    private static final Long id = 1L;

    @Mock
    private AdminService service;

    @InjectMocks
    private AdminController controller;

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
