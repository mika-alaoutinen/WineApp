package com.mika.WineApp.security;

import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.user.User;

public interface SecurityUtilities {
    /**
     * Retrieves the username from the SecurityContext.
     * @return username as string
     */
    String getUsernameFromSecurityContext();

    /**
     * Check if a given user has admin role.
     * @param user to check
     * @return boolean
     */
    boolean isUserAdmin(User user);

    /**
     * Validates an edit or delete action on wines and reviews before committing it.
     * Checks that the user was from a user that is either
     * 1) an admin or
     * 2) the owner of the item being edited.
     * @param model wine or review
     * @return boolean
     */
    boolean isUserAllowedToEdit(EntityModel model, User user);
}
