package com.mika.WineApp.security;

import com.mika.WineApp.models.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: " + e.getMessage());
        }

        return false;
    }

    private Date getExpirationDate() {
        return new Date(new Date().getTime() + jwtExpiration);
    }
}
