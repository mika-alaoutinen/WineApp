package com.mika.WineApp.errors.forbidden;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class ForbiddenAdvice {
    @ResponseBody
    @ExceptionHandler
    String ForbiddenHandler(ForbiddenException e) {
        return e.getMessage();
    }
}
