package com.mika.WineApp.security;

import com.mika.WineApp.errors.forbidden.ForbiddenException;
import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtilitiesImpl implements SecurityUtilities {

    public String getUsernameFromSecurityContext() {
        return getUserPrincipal().getUsername();
    }

    public void validateUpdateRequest(EntityModel model, User user) {
        Long userId = getUserPrincipal().getId();
        boolean isAdmin = isUserAdmin(user);
        boolean isOwner = isUserOwnerOfModel(model, userId);

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException(model);
        }
    }

    private boolean isUserAdmin(User user) {
        return user.getRoles().contains(Role.ROLE_ADMIN);
    }

    private boolean isUserOwnerOfModel(EntityModel model, Long userId) {
        return model
                .getUser()
                .getId()
                .equals(userId);
    }

    private UserPrincipal getUserPrincipal() {
        return (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
