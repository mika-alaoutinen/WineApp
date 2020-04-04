package com.mika.WineApp.errors;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class InvalidDateRangeAdvice {
    @ResponseBody
    @ExceptionHandler
    String invalidDateRangeHandler(InvalidDateRangeException e) {
        return e.getMessage();
    }
}
