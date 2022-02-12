package com.mika.WineApp.errors;

import com.mika.WineApp.reviews.model.Review;
import com.mika.WineApp.wines.model.Wine;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(Review review) {
        super("Tried to modify a review that you do not own!");
    }

    public ForbiddenException(Wine wine) {
        super("Tried to modify a wine that you do not own!");
    }
}
