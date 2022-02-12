package com.mika.WineApp.services;

import com.mika.WineApp.entities.EntityModel;
import com.mika.WineApp.errors.NotFoundException;

public interface UserService {

    /**
     * Return the username of the logged in user.
     *
     * @return username as String
     */
    String getUsername();

    /**
     * Checks if user is logged in.
     *
     * @return boolean
     */
    boolean isLoggedIn();

    /**
     * Is currently logged in user allowed to edit or delete the given model.
     *
     * @param model review or wine
     * @return boolean
     */
    boolean isUserAllowedToEdit(EntityModel model);

    /**
     * Sets the currently logged in user as the owner of a review or wine.
     *
     * @param model review or wine
     * @return review or wine
     * @throws NotFoundException e
     */
    EntityModel setUser(EntityModel model) throws NotFoundException;
}
