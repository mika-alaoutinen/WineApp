package com.mika.WineApp.errors;

import com.mika.WineApp.entities.User;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.entities.WineType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(WineType type, String givenType) {
        super("Requested wine type " + givenType + " does not exist.");
    }

    public BadRequestException(Wine wine, String wineName) {
        super("Wine with name " + wineName + " already exists!");
    }

    public BadRequestException(User user, String username) {
        super("Username " + username + " already exists!");
    }
}
