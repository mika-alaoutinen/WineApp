package com.mika.WineApp.security;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.forbidden.ForbiddenException;
import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.user.User;

public interface SecurityUtilities {
    /**
     * Retrieves the username from the SecurityContext.
     * @return username as string
     */
    String getUsernameFromSecurityContext();

    /**
     * Validates an edit or delete action on wines and reviews before committing it.
     * Checks that the user was from a user that is either
     * 1) an admin or
     * 2) the owner of the item being edited.
     * @param model wine or review
     * @throws BadRequestException e
     */
    void validateUpdateRequest(EntityModel model, User user) throws ForbiddenException;
}
