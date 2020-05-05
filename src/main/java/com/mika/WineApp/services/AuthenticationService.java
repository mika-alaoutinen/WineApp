package com.mika.WineApp.services;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;

public interface AuthenticationService {

    /**
     * Generate new JWT token once user has been authenticated.
     * @param user with credentials.
     * @return JWT token.
     */
    JwtToken login(User user);

    /**
     * Persist a new user account to database.
     * @param user new user.
     * @return saved user.
     * @throws BadRequestException e
     */
    User register(User user) throws BadRequestException;
}
