package com.mika.WineApp.users;

import com.mika.WineApp.entities.Role;
import com.mika.WineApp.entities.User;
import com.mika.WineApp.errors.BadRequestException;
import com.mika.WineApp.infra.db.UserSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
class UserService implements UserReader, UserWriter, UserSaver {
    private final UserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new BadRequestException(user, user.getUsername());
        }
        return repository.save(user);
    }

    @Override
    public Optional<User> updateRoles(Long id, Set<Role> roles) {
        return repository
                .findById(id)
                .map(user -> {
                    user.setRoles(roles);
                    return user;
                })
                .map(repository::save);
    }
}
