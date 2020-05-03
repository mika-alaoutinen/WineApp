package com.mika.WineApp.security;

import com.mika.WineApp.models.superclasses.EntityModel;
import com.mika.WineApp.security.model.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtilitiesImpl implements SecurityUtilities {

    public String getUsernameFromSecurityContext() {
        return getUserPrincipal().getUsername();
    }

    public boolean isUpdateRequestValid(EntityModel model) {
        Long ownerId = model.getUser().getId();
        Long userId = getUserPrincipal().getId();
        return ownerId.equals(userId);
    }

    private UserPrincipal getUserPrincipal() {
        return (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
