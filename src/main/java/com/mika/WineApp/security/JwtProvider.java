package com.mika.WineApp.security;

import org.springframework.security.core.Authentication;

public interface JwtProvider {

    /**
     * Generates a new JWT token from Authentication.
     * @param authentication Authentication
     * @return JWT token as string
     */
    String generateJwtToken(Authentication authentication);

    /**
     * Parses JWT token for username.
     * @param token JWT token
     * @return username
     */
    String getUserNameFromToken(String token);

    /**
     * Validates JWT token.
     * @param authToken as string
     * @return boolean indicating if token is valid
     */
    boolean validateJwtToken(String authToken);
}
