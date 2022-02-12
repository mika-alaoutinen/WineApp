package com.mika.WineApp.services;

import com.mika.WineApp.errors.BadRequestException;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.security.model.UserCredentials;
import com.mika.WineApp.users.model.User;

public interface AuthenticationService {

    /**
     * Generate new JWT token once user has been authenticated.
     *
     * @param credentials with username and password.
     * @return JWT token.
     */
    JwtToken login(UserCredentials credentials);

    /**
     * Persist a new user account to database.
     *
     * @param credentials with username and password.
     * @return saved user.
     * @throws BadRequestException e
     */
    User register(UserCredentials credentials) throws BadRequestException;
}
