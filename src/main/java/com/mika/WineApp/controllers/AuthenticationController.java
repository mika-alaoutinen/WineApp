package com.mika.WineApp.controllers;

import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Contains operations for login, logout and registering a new user.")
public class AuthenticationController {
    private final UserService service;

// Public operations available to everyone:
    @Operation(summary = "Login to receive JWT token", description = "Login with valid username and password. On successful login, API returns a JWT token.")
    @PostMapping("login")
    public JwtToken login(@Valid @RequestBody User user) {
        return service.loginUser(user);
    }

    @Operation(summary = "New user registration", description = "Register a new user.")
    @PostMapping("register")
    public User register(@Valid @RequestBody User newUser) {
        return service.registerUser(newUser);
    }

// Restricted operations limited to users with admin role:
    @GetMapping("search/id/{id}")
    public User findByUsername(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("search/username/{username}")
    public User findByUsername(@PathVariable String username) {
        return service.findByUserName(username);
    }

    @Operation(summary = "Update user's roles", description = "Replace user's old roles with roles given in request body.")
    @PutMapping("{id}/roles")
    public User updateRoles(@PathVariable Long id, @RequestBody Set<Role> roles) {
        return service.updateRoles(id, roles);
    }
}
