package com.mika.WineApp.controllers;

import com.mika.WineApp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Users API", description = "Contains operations for retrieving publicly available information about users.")
public class UserController {
    private final UserService service;

    @Operation(summary = "Get username", description = "Get username for logged in user.")
    @GetMapping("username")
    public String getUsername() {
        return service.getUsername();
    }

    @Operation(summary = "Check if user is logged in", description = "Returns true if request has a valid JWT token in headers.")
    @GetMapping("logged")
    public boolean isLoggedIn() {
        return service.isLoggedIn();
    }
}
