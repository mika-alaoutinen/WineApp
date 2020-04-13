package com.mika.WineApp.errors.wine;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

class WineNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler
    String wineNotFoundHandler(WineNotFoundException e) {
        return e.getMessage();
    }
}