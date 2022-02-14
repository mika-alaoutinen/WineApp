package com.mika.WineApp.infra.security;

import com.mika.WineApp.models.JwtToken;
import com.mika.WineApp.models.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {
    private static final String USERNAME = "test_user";
    private static final JwtToken CREDENTIALS = new JwtToken("token");
    private static final JwtProvider JWT_PROVIDER = new JwtProviderImpl(18000000, "secret");

    @Mock
    private UserPrincipal userPrincipal;

    @Test
    void shouldGenerateToken() {
        when(userPrincipal.getUsername()).thenReturn(USERNAME);
        String token = JWT_PROVIDER.generateJwtToken(new TestingAuthenticationToken(userPrincipal, CREDENTIALS));
        assertTrue(token.length() > 100);
    }

    @Test
    void shouldGetUsernameFromToken() {
        Date expiration = new Date(new Date().getTime() + 18000000);
        String token = createToken(expiration);
        assertEquals(USERNAME, JWT_PROVIDER.getUserNameFromToken(token));
    }

    @Test
    void shouldValidateToken() {
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0X3VzZXIiLCJpYXQiOjE2NDQ4NTk4OTMsImV4cCI6MTY0NDg3Nzg5M30.gDg6qbka9_AoVJfcFBh4QJV8DirvKSvR5wJNUaZlHLk";
        assertTrue(JWT_PROVIDER.validateJwtToken(validToken));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"ey.abc"})
    void notValidWhenMalformedToken(String invalidToken) {
        assertFalse(JWT_PROVIDER.validateJwtToken(invalidToken));
    }

    @Test
    void notValidWhenExpired() throws ParseException {
        Date expired = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        String token = createToken(expired);
        assertFalse(JWT_PROVIDER.validateJwtToken(token));
    }

    private static String createToken(Date expiration) {
        return Jwts
                .builder()
                .setSubject(USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }
}
