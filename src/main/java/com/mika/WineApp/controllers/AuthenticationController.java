package com.mika.WineApp.controllers;

import com.mika.WineApp.mappers.AuthenticationMapper;
import com.mika.WineApp.mappers.UserMapper;
import com.mika.WineApp.services.AuthenticationService;
import com.mika.api.AuthenticationApi;
import com.mika.model.JwtTokenDTO;
import com.mika.model.UserCredentialsDTO;
import com.mika.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {
    private final AuthenticationMapper authMapper;
    private final UserMapper userMapper;
    private final AuthenticationService service;

    @Override
    public ResponseEntity<JwtTokenDTO> login(UserCredentialsDTO userCredentialsDTO) {
        var token = service.login(authMapper.toCredentials(userCredentialsDTO));
        return ResponseEntity.ok(authMapper.toTokenDTO(token));
    }

    @Override
    public ResponseEntity<UserDTO> register(UserCredentialsDTO userCredentialsDTO) {
        var newUser = service.register(authMapper.toCredentials(userCredentialsDTO));
        return ResponseEntity.ok(userMapper.toUserDTO(newUser));
    }
}
