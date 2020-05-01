package com.mika.WineApp.services;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;
import org.springframework.security.core.Authentication;

public interface UserService {

    /**
     * Generate new JWT token once user has been authenticated.
     * @param authentication token processed by the AuthenticationManager.
     * @return JWT token.
     */
    JwtToken loginUser(Authentication authentication);

    /**
     * Persist a new user account to database.
     * @param user new user.
     * @return saved user.
     */
    User registerUser(User user);
}
