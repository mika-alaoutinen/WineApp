package com.mika.WineApp.controllers;

import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "Contains operations for managing users. All operations are restricted to admins.")
public class AdminController {
    private final AdminService service;

    @Operation(summary = "Find all users", description = "Find all saved users.")
    @GetMapping()
    public List<User> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Find user by ID", description = "Find a user by user ID.")
    @GetMapping("id/{id}")
    public User findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Find user by username", description = "Find a user by username.")
    @GetMapping("username/{username}")
    public User findByUsername(@PathVariable String username) {
        return service.findByUserName(username);
    }

    @Operation(summary = "Update user's roles", description = "Replace user's old roles with roles given in request body.")
    @PutMapping("{id}/roles")
    public User updateRoles(@PathVariable Long id, @Valid @RequestBody Set<Role> roles) {
        return service.updateRoles(id, roles);
    }
}
