package com.mika.WineApp.authentication;

import com.mika.WineApp.infra.security.SecurityUtilities;
import com.mika.WineApp.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserInfoServiceImpl implements UserInfoService {
    private final SecurityUtilities security;

    @Override
    public String getUsername() {
        return security
                .getUsernameFromSecurityContext()
                .orElse("");
    }

    @Override
    public boolean isLoggedIn() {
        return security
                .getUsernameFromSecurityContext()
                .isPresent();
    }
}
