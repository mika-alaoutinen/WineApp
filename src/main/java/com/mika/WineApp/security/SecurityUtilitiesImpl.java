package com.mika.WineApp.security;

import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtilitiesImpl implements SecurityUtilities {

    @Override
    public String getUsernameFromSecurityContext() {
        return getUserPrincipal().getUsername();
    }

    @Override
    public boolean isUserAdmin(User user) {
        return user
                .getRoles()
                .contains(Role.ROLE_ADMIN);
    }

    @Override
    public boolean isUserAllowedToEdit(EntityModel model, User user) {
        Long userId = getUserPrincipal().getId();

        boolean isAdmin = isUserAdmin(user);
        boolean isOwner = isUserOwnerOfEntity(model, userId);

        return isAdmin || isOwner;
    }

    private static boolean isUserOwnerOfEntity(EntityModel model, Long userId) {
        return model
                .getUser()
                .getId()
                .equals(userId);
    }

    private static UserPrincipal getUserPrincipal() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return principal.equals("anonymousUser")
               ? new UserPrincipal(new User())
               : (UserPrincipal) principal;
    }
}
