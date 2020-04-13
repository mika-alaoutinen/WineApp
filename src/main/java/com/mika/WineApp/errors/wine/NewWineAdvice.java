package com.mika.WineApp.errors.wine;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class NewWineAdvice {
    @ResponseBody
    @ExceptionHandler
    String newWineHandler(NewWineException e) {
        return e.getMessage();
    }
}
