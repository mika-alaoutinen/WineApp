package com.mika.WineApp.services;

import com.mika.WineApp.entities.Role;
import com.mika.WineApp.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminService {

    /**
     * Find all registered users.
     *
     * @return list of all users.
     */
    List<User> findAll();

    /**
     * Find user by ID.
     *
     * @param id user id.
     * @return user or empty.
     */
    Optional<User> findById(Long id);

    /**
     * Find user by username.
     *
     * @param username string.
     * @return user or empty.
     */
    Optional<User> findByUsername(String username);

    /**
     * Update user's roles. New roles override possible old roles.
     *
     * @param id    for user to give role to.
     * @param roles new set of roles for the user.
     * @return updated user or empty.
     */
    Optional<User> updateRoles(Long id, Set<Role> roles);
}
