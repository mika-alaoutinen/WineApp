package com.mika.WineApp.security;

import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.Role;
import com.mika.WineApp.models.User;
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
        return isUserAdmin(user) || isUserOwnerOfEntity(model, getUserPrincipal());
    }

    private static boolean isUserOwnerOfEntity(EntityModel model, UserPrincipal principal) {
        return model
                .getUser()
                .getUsername()
                .equals(principal.getUsername());
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
