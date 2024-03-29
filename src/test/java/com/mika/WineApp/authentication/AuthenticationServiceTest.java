package com.mika.WineApp.authentication;

import com.mika.WineApp.entities.User;
import com.mika.WineApp.infra.security.JwtProvider;
import com.mika.WineApp.models.JwtToken;
import com.mika.WineApp.models.UserCredentials;
import com.mika.WineApp.users.UserWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final String encodedPassword = "encoded_password";
    private static final UserCredentials CREDENTIALS = new UserCredentials("username", "password");

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserWriter writer;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @Test
    void loginReturnsJwtToken() {
        Authentication authToken = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authToken);
        when(jwtProvider.generateJwtToken(authToken)).thenReturn("Bearer jwt-token");

        JwtToken token = service.login(CREDENTIALS);

        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtProvider, times(1)).generateJwtToken(any(Authentication.class));

        assertEquals("Bearer jwt-token", token.getToken());
    }

    @Test
    void invalidLoginThrowsException() {
        var authToken = new UsernamePasswordAuthenticationToken(CREDENTIALS.username(), CREDENTIALS.password());
        String errorMessage = "Bad credentials";
        when(authenticationManager.authenticate(authToken)).thenThrow(new BadCredentialsException(errorMessage));

        Exception e = assertThrows(BadCredentialsException.class, () -> service.login(CREDENTIALS));
        assertEquals(errorMessage, e.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtProvider, times(0)).generateJwtToken(any(Authentication.class));
    }

    @Test
    void register() {
        when(writer.save(any(User.class))).thenReturn(new User(CREDENTIALS.username(), encodedPassword));
        when(passwordEncoder.encode(CREDENTIALS.password())).thenReturn(encodedPassword);

        User registeredUser = service.register(CREDENTIALS);

        verify(passwordEncoder, times(1)).encode(CREDENTIALS.password());
        verify(writer, times(1)).save(any(User.class));
        assertEquals(encodedPassword, registeredUser.getPassword());
    }
}
