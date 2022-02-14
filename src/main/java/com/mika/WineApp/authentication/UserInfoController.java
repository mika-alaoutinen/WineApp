package com.mika.WineApp.authentication;

import com.mika.WineApp.services.UserInfoService;
import com.mika.api.UserInfoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UserInfoController implements UserInfoApi {
    private final UserInfoService service;

    @Override
    public ResponseEntity<String> getUsername() {
        return ResponseEntity.ok(service.getUsername());
    }

    @Override
    public ResponseEntity<Boolean> isLoggedIn() {
        return ResponseEntity.ok(service.isLoggedIn());
    }
}
