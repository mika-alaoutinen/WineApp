package com.mika.WineApp.users;

import com.mika.WineApp.entities.Role;
import com.mika.WineApp.entities.User;

import java.util.Optional;
import java.util.Set;

public interface UserWriter {
    User save(User user);

    Optional<User> updateRoles(Long id, Set<Role> roles);
}
