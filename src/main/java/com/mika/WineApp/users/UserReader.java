package com.mika.WineApp.users;

import com.mika.WineApp.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserReader {
    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);
}
