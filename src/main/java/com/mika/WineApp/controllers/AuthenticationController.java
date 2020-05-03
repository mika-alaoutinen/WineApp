package com.mika.WineApp.controllers;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Contains operations for login, logout and registering a new user.")
public class AuthenticationController {
    private final UserService service;

    @PostMapping("login")
    public JwtToken login(@Valid @RequestBody User user) {
        return service.loginUser(user);
    }

    @PostMapping("register")
    public User register(@Valid @RequestBody User newUser) {
        return service.registerUser(newUser);
    }
}
