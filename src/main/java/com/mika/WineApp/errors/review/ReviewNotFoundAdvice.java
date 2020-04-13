package com.mika.WineApp.errors.review;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class ReviewNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler
    String reviewNotFoundHandler(ReviewNotFoundException e) {
        return e.getMessage();
    }
}
