package com.mika.WineApp.controllers.unit;

import com.mika.WineApp.controllers.UserController;
import com.mika.WineApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

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
    public void isLoggedIn() {
        controller.isLoggedIn();
        verify(service, times(1)).isLoggedIn();
    }
}
