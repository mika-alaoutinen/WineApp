package com.mika.WineApp.errors;

public class InvalidWineTypeException extends RuntimeException {

    public InvalidWineTypeException(String type) {
        super("Error: requested wine type " + type + " does not exist.");
    }
}
