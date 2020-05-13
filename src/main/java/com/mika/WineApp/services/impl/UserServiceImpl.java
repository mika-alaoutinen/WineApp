package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.security.SecurityUtilities;
import com.mika.WineApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SecurityUtilities securityUtils;
    private final UserRepository repository;

    public String getUsername() {
        return securityUtils.getUsernameFromSecurityContext();
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(new User(), id));
    }

    public User findByUserName(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(new User(), username));
    }

    public User save(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new BadRequestException(user, user.getUsername());
        }
        return repository.save(user);
    }

    public User updateRoles(Long id, Set<Role> roles) {
        User user = findById(id);
        user.setRoles(roles);
        return repository.save(user);
    }
}
