package com.mika.WineApp.errors.invaliddate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String date) {
        super("Error: could not parse date " + date + ".");
    }

    public InvalidDateException(String[] dates) {
        super("Error: date range must have a start date and an end date. Given " + Arrays.toString(dates));
    }
}
