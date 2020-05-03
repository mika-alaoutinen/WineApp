package com.mika.WineApp.security;

import com.mika.WineApp.security.model.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtilities {

    public static String getUsernameFromSecurityContext() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userPrincipal.getUsername();
    }
}
