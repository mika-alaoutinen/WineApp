package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.security.model.LoginRequest;
import com.mika.WineApp.security.model.RegisterUserRequest;
import com.mika.WineApp.security.model.UserPrincipal;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(new User(), username));

        return UserPrincipal.build(user);
    }

//    public JwtToken loginUser(LoginRequest request) {
//
//    }

    public User registerUser(RegisterUserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (repository.existsByUsername(username)) {
            throw new BadRequestException(username);
        }

        var roles = parseRoles(request.getRoles());

        return repository.save(new User(username, password, roles));
    }

    private Set<Role> parseRoles(Set<String> roles) {
        return roles.stream()
                .map(Role::new)
                .collect(Collectors.toSet());
    }
}
