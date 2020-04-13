package com.mika.WineApp.errors.wine;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NewWineException extends RuntimeException {

    public NewWineException(String name) {
        super("Error: wine with name " + name + " already exists!");
    }
}
