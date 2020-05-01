package com.mika.WineApp.errors.notfound;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class NotFoundAdvice {
    @ResponseBody
    @ExceptionHandler
    String NotFoundHandler(NotFoundException e) {
        return e.getMessage();
    }
}
