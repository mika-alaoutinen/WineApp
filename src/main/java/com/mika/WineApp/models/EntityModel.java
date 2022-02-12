package com.mika.WineApp.models;

import com.mika.WineApp.users.model.User;

public interface EntityModel {

    User getUser();

    void setUser(User user);
}
