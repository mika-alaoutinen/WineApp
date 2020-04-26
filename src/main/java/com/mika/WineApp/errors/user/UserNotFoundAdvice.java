package com.mika.WineApp.errors.user;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class UserNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler
    String UserNotFoundHandler(UserNotFoundException e) {
        return e.getMessage();
    }
}
