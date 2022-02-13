package com.mika.WineApp.infra.security;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.entities.User;
import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.users.UserReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {
    private static final User user = TestData
            .initTestUsers()
            .get(0);

    @Mock
    private UserReader reader;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Test
    void loadUserByUsername() {
        String username = user.getUsername();
        when(reader.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername(user.getUsername());
        verify(reader, times(1)).findByUsername(username);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void throwExceptionOnNonexistentUsername() {
        String username = "nonexistent user";
        when(reader.findByUsername(username)).thenReturn(Optional.empty());

        Exception e = assertThrows(NotFoundException.class, () -> service.loadUserByUsername(username));
        verify(reader, times(1)).findByUsername(username);
        assertEquals("Could not find user with username " + username, e.getMessage());
    }
}
