package com.mika.WineApp.errors.badrequest;

import com.mika.WineApp.models.superclasses.EntityModel;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.models.wine.WineType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(WineType type, String givenType) {
        super("Error: requested wine type " + givenType + " does not exist.");
    }

    public BadRequestException(Wine wine, String wineName) {
        super("Error: wine with name " + wineName + " already exists!");
    }

    public BadRequestException(User user, String username) {
        super("Error: username " + username + " already exists!");
    }

    public BadRequestException(EntityModel model) {
        super("Error: tried to modify review or wine that you do not own!");
    }
}
