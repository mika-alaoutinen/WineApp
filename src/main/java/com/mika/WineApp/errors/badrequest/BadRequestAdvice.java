package com.mika.WineApp.errors.badrequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BadRequestAdvice {
    @ResponseBody
    @ExceptionHandler
    String BadRequestHandler(BadRequestException e) {
        return e.getMessage();
    }
}
