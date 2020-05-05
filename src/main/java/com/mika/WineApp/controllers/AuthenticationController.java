package com.mika.WineApp.controllers;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Contains operations for login, logout and registering a new user. Both are public operations available to everyone")
public class AuthenticationController {
    private final AuthenticationService service;

    @Operation(summary = "Login to receive JWT token", description = "Login with valid username and password. On successful login, API returns a JWT token.")
    @PostMapping("login")
    public JwtToken login(@Valid @RequestBody User user) {
        return service.login(user);
    }

    @Operation(summary = "New user registration", description = "Register a new user.")
    @PostMapping("register")
    public User register(@Valid @RequestBody User newUser) {
        return service.register(newUser);
    }
}
