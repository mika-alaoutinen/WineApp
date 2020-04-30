package com.mika.WineApp.controllers;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.services.UserService;
import com.mika.WineApp.services.impl.UserServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService service;

    public UserController(UserRepository repository) {
        this.service = new UserServiceImpl(repository);
    }

    @PostMapping("register")
    public User addAccount(@RequestBody User newAccount) {
        return service.addUser(newAccount);
    }
}
