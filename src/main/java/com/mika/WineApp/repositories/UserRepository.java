package com.mika.WineApp.repositories;

import com.mika.WineApp.models.User;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepository extends Repository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    User save(User account);
}
