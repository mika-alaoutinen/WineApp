package com.mika.WineApp.errors.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtExpiredException extends JwtException {

    public JwtExpiredException(String token) {
        super("Given JWT token has expired! Please log in again.");
    }
}
