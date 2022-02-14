package com.mika.WineApp.authentication;

import com.mika.WineApp.entities.EntityModel;

public interface UserAuthentication {

    /**
     * Is currently logged-in user allowed to edit or delete the given entity.
     *
     * @param entity review or wine
     * @return boolean
     */
    boolean isUserAllowedToEdit(EntityModel entity);

    /**
     * Sets the currently logged-in user as the owner of a review or wine.
     *
     * @param model review or wine
     * @return review or wine
     */
    EntityModel setUser(EntityModel model);
}
