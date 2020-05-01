package com.mika.WineApp.services;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.RegisterUserRequest;
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
     * @param request containing new user's credentials.
     * @return saved user.
     */
    User registerUser(RegisterUserRequest request);
}
