package com.mika.WineApp.errors.badrequest;

import com.mika.WineApp.models.WineType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(WineType type, String givenType) {
        super("Error: requested wine type " + givenType + " does not exist.");
    }

    public BadRequestException(String wineName) {
        super("Error: wine with name " + wineName + " already exists!");
    }
}
