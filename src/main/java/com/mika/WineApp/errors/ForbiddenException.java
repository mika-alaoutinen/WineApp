package com.mika.WineApp.errors;

import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(Review review) {
        super("Error: tried to modify a review that you do not own!");
    }

    public ForbiddenException(Wine wine) {
        super("Error: tried to modify a wine that you do not own!");
    }
}
