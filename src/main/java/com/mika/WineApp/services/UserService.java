package com.mika.WineApp.services;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.JwtToken;

import java.util.List;
import java.util.Set;

public interface UserService {

    /**
     * Find all users.
     * @return list of users
     */
    List<User> findAll();

    /**
     * Find user by ID.
     * @param id of user
     * @return User
     * @throws NotFoundException e
     */
    User findById(Long id) throws NotFoundException;

    /**
     * Find a user by username. Throw exception if user is not found.
     * @param username, which is unique.
     * @return User
     * @throws NotFoundException e
     */
    User findByUserName(String username) throws NotFoundException;

    /**
     * Update user's roles. New roles override possible old roles.
     * @param username for user to give role to
     * @param roles new set of roles for the user.
     * @return updated user
     */
    User updateRoles(Long username, Set<Role> roles) throws NotFoundException;

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
     * @throws BadRequestException e
     */
    User registerUser(User user) throws BadRequestException;
}
