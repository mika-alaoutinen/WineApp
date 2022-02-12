package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.BadRequestException;
import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.services.AdminService;
import com.mika.WineApp.users.UserRepository;
import com.mika.WineApp.users.model.Role;
import com.mika.WineApp.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(new User(), id));
    }

    @Override
    public User findByUserName(String username) {
        return repository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException(new User(), username));
    }

    @Override
    public User save(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new BadRequestException(user, user.getUsername());
        }
        return repository.save(user);
    }

    @Override
    public User updateRoles(Long id, Set<Role> roles) {
        User user = findById(id);
        user.setRoles(roles);
        return repository.save(user);
    }
}
