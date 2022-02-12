package com.mika.WineApp.services;

import com.mika.WineApp.errors.BadRequestException;
import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.users.model.Role;
import com.mika.WineApp.users.model.User;

import java.util.Set;

/**
 * Allows read and write access to the user repository.
 */
public interface AdminService extends UserRepositoryReader {

    /**
     * Saves a new user. User's password must be encoded before saving it.
     *
     * @param user to save
     * @return saved user
     * @throws BadRequestException e
     */
    User save(User user) throws BadRequestException;

    /**
     * Update user's roles. New roles override possible old roles.
     *
     * @param id    for user to give role to
     * @param roles new set of roles for the user.
     * @return updated user
     * @throws NotFoundException e
     */
    User updateRoles(Long id, Set<Role> roles) throws NotFoundException;
}
