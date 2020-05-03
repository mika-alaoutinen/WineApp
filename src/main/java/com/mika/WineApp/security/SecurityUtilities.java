package com.mika.WineApp.security;

import com.mika.WineApp.models.superclasses.EntityModel;
import com.mika.WineApp.security.model.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtilities {

    public static String getUsernameFromSecurityContext() {
        return getUserPrincipal().getUsername();
    }

    public static boolean isUpdateRequestValid(EntityModel model) {
        Long ownerId = model.getUser().getId();
        Long userId = getUserPrincipal().getId();
        return ownerId.equals(userId);
    }

    private static UserPrincipal getUserPrincipal() {
        return (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
