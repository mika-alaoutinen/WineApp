package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.notfound.NotFoundException;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(new User(), id));
    }

    public User findByUserName(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(new User(), username));
    }

    public JwtToken loginUser(User user) {
        Authentication authentication = getAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new JwtToken(jwt);
    }

    public User registerUser(User newUser) {
        String username = newUser.getUsername();

        if (repository.existsByUsername(username)) {
            throw new BadRequestException(new User(), username);
        }

        String password = passwordEncoder.encode(newUser.getPassword());

        return repository.save(new User(username, password));
    }

    public User updateRoles(Long id, Set<Role> roles) {
        User user = findById(id);
        user.setRoles(roles);

        return repository.save(user);
    }

    private Authentication getAuthentication(User user) {
        var token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return authenticationManager.authenticate(token);
    }
}
