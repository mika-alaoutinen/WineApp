package com.mika.WineApp.services;

import com.mika.WineApp.models.User;
import com.mika.WineApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account = repository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(account.getUsername())
                .password("{noop}" + account.getPassword()) // Password storage format must be explicitly specified as {noop}
                .authorities("USER")
                .build();
    }

    public User addUser(User newAccount) {
        return new User();
    }
}
