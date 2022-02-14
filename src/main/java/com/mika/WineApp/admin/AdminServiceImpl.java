package com.mika.WineApp.admin;

import com.mika.WineApp.entities.Role;
import com.mika.WineApp.entities.User;
import com.mika.WineApp.services.AdminService;
import com.mika.WineApp.users.UserReader;
import com.mika.WineApp.users.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
class AdminServiceImpl implements AdminService {
    private final UserReader reader;
    private final UserWriter writer;

    @Override
    public List<User> findAll() {
        return reader.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return reader.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return reader.findByUsername(username);
    }

    @Override
    public Optional<User> updateRoles(Long id, Set<Role> roles) {
        return writer.updateRoles(id, roles);
    }
}
