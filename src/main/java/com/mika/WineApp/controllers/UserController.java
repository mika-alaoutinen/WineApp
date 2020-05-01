package com.mika.WineApp.controllers;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.security.model.LoginRequest;
import com.mika.WineApp.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "User API", description = "Contains operations for login and registering a new user.")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService service;

    @PostMapping("/login")
    public JwtToken authenticate(@Valid @RequestBody LoginRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        return service.loginUser(authentication);
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody User newUser) {
        return service.registerUser(newUser);
    }
}
