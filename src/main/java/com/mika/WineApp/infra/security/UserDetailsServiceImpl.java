package com.mika.WineApp.infra.security;

import com.mika.WineApp.entities.User;
import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.models.UserPrincipal;
import com.mika.WineApp.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException(new User(), username));

        return new UserPrincipal(user);
    }
}