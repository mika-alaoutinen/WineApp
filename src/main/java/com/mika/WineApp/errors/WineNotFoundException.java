package com.mika.WineApp.errors;

public class WineNotFoundException extends RuntimeException {

    WineNotFoundException(Long id) {
        super("Error: could not find wine with id " + id);
    }
}