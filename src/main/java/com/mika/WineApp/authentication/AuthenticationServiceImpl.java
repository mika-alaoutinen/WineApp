package com.mika.WineApp.authentication;

import com.mika.WineApp.entities.User;
import com.mika.WineApp.errors.BadRequestException;
import com.mika.WineApp.infra.security.JwtProvider;
import com.mika.WineApp.models.JwtToken;
import com.mika.WineApp.models.UserCredentials;
import com.mika.WineApp.services.AdminService;
import com.mika.WineApp.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl implements AuthenticationService {
    private final AdminService service;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtToken login(UserCredentials credentials) {
        Authentication authentication = getAuthentication(credentials);
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        return new JwtToken(jwt);
    }

    @Override
    public User register(UserCredentials credentials) throws BadRequestException {
        String username = credentials.username();
        String password = passwordEncoder.encode(credentials.password());
        return service.save(new User(username, password));
    }

    private Authentication getAuthentication(UserCredentials credentials) {
        var token = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
        return authenticationManager.authenticate(token);
    }
}
