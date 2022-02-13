package com.mika.WineApp.authentication;

import com.mika.WineApp.entities.EntityModel;
import com.mika.WineApp.infra.security.SecurityUtilities;
import com.mika.WineApp.users.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthenticationUtils implements UserAuthentication {
    private final UserReader reader;
    private final SecurityUtilities security;

    @Override
    public boolean isUserAllowedToEdit(EntityModel entity) {
        return security
                .getUsernameFromSecurityContext()
                .flatMap(reader::findByUsername)
                .map(user -> security.isUserAllowedToEdit(entity, user))
                .orElse(false);
    }

    @Override
    public EntityModel setUser(EntityModel model) {
        security
                .getUsernameFromSecurityContext()
                .flatMap(reader::findByUsername)
                .ifPresent(model::setUser);

        return model;
    }
}