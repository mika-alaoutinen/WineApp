package com.mika.WineApp.models;

import com.mika.WineApp.models.user.User;

public interface EntityModel {

    User getUser();

    void setUser(User user);
}
