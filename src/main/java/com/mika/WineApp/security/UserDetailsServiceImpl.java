package com.mika.WineApp.security;

import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.models.User;
import com.mika.WineApp.security.model.UserPrincipal;
import com.mika.WineApp.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    /**
     * Find user by username from repository.
     *
     * @param username as string
     * @return UserDetails
     * @throws UsernameNotFoundException e
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException(new User(), username));

        return new UserPrincipal(user);
    }
}
