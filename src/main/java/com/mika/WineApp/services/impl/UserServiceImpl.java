package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.security.JwtProvider;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public JwtToken loginUser(User user) {
        Authentication authentication = getAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new JwtToken(jwt);
    }

    public User registerUser(User newUser) {
        String username = newUser.getUsername();

        if (repository.existsByUsername(username)) {
            throw new BadRequestException(username);
        }

        String password = passwordEncoder.encode(newUser.getPassword());
        var roles = getUserRoles(newUser);

        return repository.save(new User(username, password, roles));
    }

    private Authentication getAuthentication(User user) {
        var token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return authenticationManager.authenticate(token);
    }

    private Set<Role> getUserRoles(User user) {
        return user.getRoles() == null
                ? new HashSet<>()
                : user.getRoles();
    }
}