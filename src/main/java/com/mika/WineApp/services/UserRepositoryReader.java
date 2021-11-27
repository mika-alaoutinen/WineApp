package com.mika.WineApp.services;

import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.models.user.User;

import java.util.List;

/**
 * Allows read access to user repository.
 */
public interface UserRepositoryReader {

    /**
     * Find all users.
     *
     * @return list of users
     */
    List<User> findAll();

    /**
     * Find user by ID.
     *
     * @param id of user
     * @return User
     * @throws NotFoundException e
     */
    User findById(Long id) throws NotFoundException;

    /**
     * Find a user by username. Throw exception if user is not found.
     *
     * @param username, which is unique.
     * @return User
     * @throws NotFoundException e
     */
    User findByUserName(String username) throws NotFoundException;
}
