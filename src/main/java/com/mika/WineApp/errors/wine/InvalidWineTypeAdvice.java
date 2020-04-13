package com.mika.WineApp.errors.wine;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class InvalidWineTypeAdvice {
    @ResponseBody
    @ExceptionHandler
    String invalidWineTypeHandler(InvalidWineTypeException e) {
        return e.getMessage();
    }
}