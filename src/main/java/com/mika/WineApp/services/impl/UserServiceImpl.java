package com.mika.WineApp.services.impl;

import com.mika.WineApp.models.EntityModel;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.SecurityUtilities;
import com.mika.WineApp.services.AdminService;
import com.mika.WineApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AdminService service;
    private final SecurityUtilities securityUtils;

    public String getUsername() {
        return securityUtils.getUsernameFromSecurityContext();
    }

    public boolean isLoggedIn() {
        String username = securityUtils.getUsernameFromSecurityContext();
        return username != null;
    }

    public boolean isUserAllowedToEdit(EntityModel model) {
        String username = securityUtils.getUsernameFromSecurityContext();
        if (username == null) {
            return false;
        }

        User user = service.findByUserName(username);
        return securityUtils.isUserAllowedToEdit(model, user);
    }

    public EntityModel setUser(EntityModel model) {
        String username = securityUtils.getUsernameFromSecurityContext();
        User user = service.findByUserName(username);
        model.setUser(user);
        return model;
    }
}
