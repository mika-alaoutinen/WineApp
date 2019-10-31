package com.mika.WineApp.errors;

public class ReviewNotFoundException extends RuntimeException {

    ReviewNotFoundException(Long id) {
        super("Error: could not find review with id " + id);
    }
}
