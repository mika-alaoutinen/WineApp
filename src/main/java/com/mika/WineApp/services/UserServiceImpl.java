package com.mika.WineApp.services;

import com.mika.WineApp.errors.user.UserNotFoundException;
import com.mika.WineApp.models.User;
import com.mika.WineApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> account = repository.findByUsername(username);

        return account.map(user -> org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .build()
        ).orElseThrow(() -> new UserNotFoundException(username));
    }

    public User addUser(User newAccount) {
        return new User();
    }
}
