package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.JwtProvider;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.services.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    private final static String encodedPassword = "encoded_password";
    private final static User user = TestData.createTestUsers().get(0);

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @Test
    public void loginReturnsJwtToken() {
        var authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        Mockito.when(authenticationManager.authenticate(authToken))
                .thenReturn(authToken);

        Mockito.when(jwtProvider.generateJwtToken(authToken))
                .thenReturn("Bearer jwt-token");

        JwtToken token = service.login(user);

        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtProvider, times(1)).generateJwtToken(any(Authentication.class));

        assertEquals(authToken, SecurityContextHolder.getContext().getAuthentication());
        assertEquals("Bearer jwt-token", token.getToken());
    }

    @Test
    public void invalidLoginThrowsException() {
        var authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        String errorMessage = "Bad credentials";

        Mockito.when(authenticationManager.authenticate(authToken))
                .thenThrow(new BadCredentialsException(errorMessage));

        Exception e = assertThrows(BadCredentialsException.class, () -> service.login(user));
        assertEquals(errorMessage, e.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtProvider, times(0)).generateJwtToken(any(Authentication.class));
    }

    @Test
    public void register() {
        User newUser = new User(user.getUsername(), encodedPassword);

        Mockito.when(userService.save(any(User.class)))
               .thenReturn(newUser);

        Mockito.when(passwordEncoder.encode(user.getPassword()))
                .thenReturn(encodedPassword);

        User registeredUser = service.register(user);

        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userService, times(1)).save(newUser);
        assertEquals(encodedPassword, registeredUser.getPassword());
    }
}
