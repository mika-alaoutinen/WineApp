package com.mika.WineApp.services;

import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;

public interface UserService {

    /**
     * Find a user by username. Throw exception if user is not found.
     * @param username, which is unique.
     * @return User
     * @throws NotFoundException e
     */
    User findByUserName(String username);

    /**
     * Generate new JWT token once user has been authenticated.
     * @param user with credentials.
     * @return JWT token.
     */
    JwtToken loginUser(User user);

    /**
     * Persist a new user account to database.
     * @param user new user.
     * @return saved user.
     */
    User registerUser(User user);
}
