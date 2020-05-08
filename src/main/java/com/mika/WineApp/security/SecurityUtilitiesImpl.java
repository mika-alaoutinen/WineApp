package com.mika.WineApp.security;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.security.model.UserPrincipal;
import com.mika.WineApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtilitiesImpl implements SecurityUtilities {
    private final UserService service;

    public String getUsernameFromSecurityContext() {
        return getUserPrincipal().getUsername();
    }

    public void validateUpdateRequest(EntityModel model) {
        Long userId = getUserPrincipal().getId();
        boolean isAdmin = isUserAdmin(userId);
        boolean isOwner = isUserOwnerOfModel(model, userId);

        if (!isAdmin && !isOwner) {
            throw new BadRequestException(model);
        }
    }

    private boolean isUserAdmin(Long userId) {
        return service
                .findById(userId)
                .getRoles()
                .contains(Role.ROLE_ADMIN);
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
