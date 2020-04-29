package com.mika.WineApp.repositories;

import com.mika.WineApp.models.User;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserRepository extends Repository<User, Long> {
    User findById(Long id);
    User findByUsername(String username);
    User save(User account);
}
