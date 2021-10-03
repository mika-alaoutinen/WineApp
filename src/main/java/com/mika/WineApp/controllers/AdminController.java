package com.mika.WineApp.controllers;

import com.mika.WineApp.mappers.RoleMapper;
import com.mika.WineApp.mappers.UserMapper;
import com.mika.WineApp.services.AdminService;
import com.mika.api.AdminApi;
import com.mika.model.RoleDTO;
import com.mika.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final AdminService service;

    @Override
    public ResponseEntity<UserDTO> findUserById(Long id) {
        var user = userMapper.toUserDTO(service.findById(id));
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<UserDTO> findUserByUsername(String username) {
        var user = userMapper.toUserDTO(service.findByUserName(username));
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        var users = service
                .findAll()
                .stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserDTO> updateRoles(Long id, Set<RoleDTO> newRoles) {
        var roles = newRoles
                .stream()
                .map(roleMapper::toModel)
                .collect(Collectors.toSet());

        var updatedUser = service.updateRoles(id, roles);
        return ResponseEntity.ok(userMapper.toUserDTO(updatedUser));
    }
}
