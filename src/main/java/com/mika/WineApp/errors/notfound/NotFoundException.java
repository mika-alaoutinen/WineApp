package com.mika.WineApp.errors.notfound;

import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(Wine wine, Long id) {
        super("Error: could not find wine with id " + id);
    }

    public NotFoundException(Review review, Long id) {
        super("Error: could not find review with id " + id);
    }

    public NotFoundException(User user, String username) {
        super("Error: could not find user with username " + username);
    }
}
