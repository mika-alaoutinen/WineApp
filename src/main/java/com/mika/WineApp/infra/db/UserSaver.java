package com.mika.WineApp.infra.db;

import com.mika.WineApp.entities.User;

import java.util.Optional;

public interface UserSaver {
    Optional<User> findByUsername(String username);

    User save(User user);
}
