package com.mika.WineApp.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String resourceType) {
        super(String.format("Tried to modify a %s that you do not own!", resourceType));
    }
}
