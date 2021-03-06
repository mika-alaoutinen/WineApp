package com.mika.WineApp.errors.invaliddate;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class InvalidDateAdvice {
    @ResponseBody
    @ExceptionHandler
    String invalidDateHandler(InvalidDateException e) {
        return e.getMessage();
    }
}
