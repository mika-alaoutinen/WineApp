package com.mika.WineApp.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String date) {
        super("Could not parse date " + date);
    }

    public InvalidDateException(List<String> dates) {
        super("Date range must have a start date and an end date. Given " + dates);
    }
}
