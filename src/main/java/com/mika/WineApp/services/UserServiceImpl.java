package com.mika.WineApp.services;

import com.mika.WineApp.models.UserAccount;
import com.mika.WineApp.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = repository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(account.getUsername())
                .password("{noop}" + account.getPassword()) // Password storage format must be explicitly specified as {noop}
                .authorities("USER")
                .build();
    }
}
