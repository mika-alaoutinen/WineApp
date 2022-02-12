package com.mika.WineApp.errors;

import com.mika.WineApp.reviews.model.Review;
import com.mika.WineApp.users.model.User;
import com.mika.WineApp.wines.model.Wine;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(Wine wine, Long id) {
        super("Could not find wine with id " + id);
    }

    public NotFoundException(Review review, Long id) {
        super("Could not find review with id " + id);
    }

    public NotFoundException(User user, Long id) {
        super("Could not find user with id " + id);
    }

    public NotFoundException(User user, String username) {
        super("Could not find user with username " + username);
    }
}
