package com.mika.WineApp.controllers;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    private static final User user = new User();

    @Mock
    private UserService service;

    @InjectMocks
    private AuthenticationController controller;

    @Test
    public void login() {
        controller.login(user);
        verify(service, times(1)).loginUser(user);
    }

    @Test
    public void register() {
        controller.register(user);
        verify(service, times(1)).registerUser(user);
    }
}
