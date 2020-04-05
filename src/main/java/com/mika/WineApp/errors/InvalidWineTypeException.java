package com.mika.WineApp.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidWineTypeException extends RuntimeException {

    public InvalidWineTypeException(String type) {
        super("Error: requested wine type " + type + " does not exist.");
    }
}
