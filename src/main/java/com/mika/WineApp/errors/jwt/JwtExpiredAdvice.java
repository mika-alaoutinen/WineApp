package com.mika.WineApp.errors.jwt;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class JwtExpiredAdvice {
    @ResponseBody
    @ExceptionHandler
    String JwtExpiredHandler(JwtExpiredException e) {
        return e.getMessage();
    }
}
