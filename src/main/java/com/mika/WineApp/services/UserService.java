package com.mika.WineApp.services;

import com.mika.WineApp.models.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    /**
     * Find user by username from repository.
     * @param username as string
     * @return UserDetails
     * @throws UsernameNotFoundException e
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Persist a new user account to database.
     * @param account to save.
     * @return saved account.
     */
    User addUser(User account);
}
