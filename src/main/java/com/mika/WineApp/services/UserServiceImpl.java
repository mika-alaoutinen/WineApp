package com.mika.WineApp.services;

import com.mika.WineApp.errors.user.UserNotFoundException;
import com.mika.WineApp.models.User;
import com.mika.WineApp.models.UserPrincipal;
import com.mika.WineApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return UserPrincipal.build(user);
    }

    public User addUser(User newAccount) {
        return new User();
    }
}
