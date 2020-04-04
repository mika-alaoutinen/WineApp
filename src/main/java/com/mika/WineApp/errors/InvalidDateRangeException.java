package com.mika.WineApp.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateRangeException extends RuntimeException {

    public InvalidDateRangeException(String[] dates) {
        super("Error: date range must have a start date and an end date. Given " + Arrays.toString(dates));
    }
}
