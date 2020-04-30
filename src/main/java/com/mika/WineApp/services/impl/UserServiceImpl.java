package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.UserPrincipal;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public User addUser(User newAccount) {
        return new User();
    }
}
