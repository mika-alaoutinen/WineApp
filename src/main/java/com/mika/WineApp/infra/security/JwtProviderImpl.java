package com.mika.WineApp.infra.security;

import com.mika.WineApp.models.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
class JwtProviderImpl implements JwtProvider {
    private final int jwtExpiration;
    private final String jwtSecret;

    JwtProviderImpl(@Value("${jwt.expiration}") int jwtExpiration, @Value("${jwt.secret}") String jwtSecret) {
        this.jwtExpiration = jwtExpiration;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public String generateJwtToken(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return Jwts
                .builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    @Override
    public String getUserNameFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
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
