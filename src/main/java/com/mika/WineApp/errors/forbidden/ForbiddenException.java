package com.mika.WineApp.errors.forbidden;

import com.mika.WineApp.models.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(EntityModel model) {
        super("Error: tried to modify review or wine that you do not own!");
    }
}
