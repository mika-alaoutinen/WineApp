package com.mika.WineApp.controllers;

import com.mika.WineApp.services.UserService;
import com.mika.api.UserInfoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserInfoApi {
    private final UserService service;

    @Override
    public ResponseEntity<String> getUsername() {
        return ResponseEntity.ok(service.getUsername());
    }

    @Override
    public ResponseEntity<Boolean> isLoggedIn() {
        return ResponseEntity.ok(service.isLoggedIn());
    }
}
