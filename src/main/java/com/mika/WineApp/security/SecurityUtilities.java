package com.mika.WineApp.security;

import com.mika.WineApp.models.superclasses.EntityModel;

public interface SecurityUtilities {
    String getUsernameFromSecurityContext();
    boolean isUpdateRequestValid(EntityModel model);

}
