package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.security.JwtProvider;
import com.mika.WineApp.security.model.JwtToken;
import com.mika.WineApp.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final static User user = TestData.createTestUsers().get(0);
    private final static String encodedPassword = "encoded_password";

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository repository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    public void loginUserReturnsJwtToken() {
        var authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        Mockito.when(authenticationManager.authenticate(authToken))
               .thenReturn(authToken);

        Mockito.when(jwtProvider.generateJwtToken(authToken))
               .thenReturn("Bearer jwt-token");

        JwtToken token = service.loginUser(user);

        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtProvider, times(1)).generateJwtToken(any(Authentication.class));

        assertEquals(authToken, SecurityContextHolder.getContext().getAuthentication());
        assertEquals("Bearer jwt-token", token.getToken());
    }

    @Test
    public void registerUserReturnsNewUser() {
        String username = user.getUsername();
        String password = user.getPassword();
        User newUser = new User(user.getUsername(), encodedPassword, user.getRoles());

        Mockito.when(repository.existsByUsername(username))
               .thenReturn(false);

        Mockito.when(passwordEncoder.encode(password))
               .thenReturn(encodedPassword);

        Mockito.when(repository.save(newUser))
                .thenReturn(newUser);

        User registeredUser = service.registerUser(user);

        verify(repository, times(1)).existsByUsername(username);
        verify(passwordEncoder, times(1)).encode(password);
        verify(repository, times(1)).save(newUser);

        assertEquals(encodedPassword, registeredUser.getPassword());
        assertTrue(registeredUser.getRoles().contains(Role.ROLE_USER));
        assertEquals(1, registeredUser.getRoles().size());
    }

    @Test
    public void createsUserWithNoRoles() {
        User registeredUser = registerValidUser(new User(user.getUsername(), encodedPassword, null));
        Set<Role> roles = registeredUser.getRoles();
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

//    @Test
    public void throwsExceptionIfUserNameIsTaken() {
        String username = user.getUsername();

    }

    private User registerValidUser(User newUser) {
        String username = user.getUsername();
        String password = user.getPassword();

        Mockito.when(repository.existsByUsername(username))
                .thenReturn(false);

        Mockito.when(passwordEncoder.encode(password))
                .thenReturn(encodedPassword);

        Mockito.when(repository.save(user))
                .thenReturn(newUser);

        User registeredUser = service.registerUser(user);

        verify(repository, times(1)).existsByUsername(username);
        verify(passwordEncoder, times(1)).encode(password);
        verify(repository, times(1)).save(newUser);

        return registeredUser;
    }
}
