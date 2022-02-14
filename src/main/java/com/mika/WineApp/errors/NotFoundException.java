package com.mika.WineApp.errors;

import com.mika.WineApp.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(User user, String username) {
        super("Could not find user with username " + username);
    }
}
