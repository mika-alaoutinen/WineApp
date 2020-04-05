package com.mika.WineApp.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WineNotFoundException extends RuntimeException {

    public WineNotFoundException(Long id) {
        super("Error: could not find wine with id " + id);
    }

    public WineNotFoundException(String name) {
        super("Error: could not find wine with name " + name);
    }
}