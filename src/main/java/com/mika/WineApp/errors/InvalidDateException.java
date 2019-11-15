package com.mika.WineApp.errors;

public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String date) {
        super("Error: could not parse date " + date + ". Enter date as yyyy-mm-dd.");
    }
}
