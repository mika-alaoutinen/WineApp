package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final static User user = TestData.createTestUsers().get(0);
    private final static Long userId = user.getId();

    @Mock
    UserRepository repository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    public void findById() {
        Mockito.when(repository.findById(userId))
               .thenReturn(Optional.of(user));

        User foundUser = service.findById(userId);
        verify(repository, times(1)).findById(userId);
        assertEquals(foundUser, user);
    }

    @Test
    public void findByIdThrowsException() {
        Mockito.when(repository.findById(userId))
               .thenReturn(Optional.empty());

        Exception e = assertThrows(NotFoundException.class, () ->
                service.findById(userId));

        verify(repository, times(1)).findById(userId);
        assertEquals("Error: could not find user with id " + userId, e.getMessage());
    }

    @Test
    public void throwsExceptionIfUserNameIsTaken() {
        String username = user.getUsername();

        Mockito.when(repository.existsByUsername(username))
               .thenReturn(true);

        Exception e = assertThrows(BadRequestException.class, () -> service.save(user));

        assertEquals("Error: username " + username + " already exists!", e.getMessage());
        verify(repository, times(1)).existsByUsername(username);
        verify(repository, times(0)).save(any(User.class));
    }

    @Test
    public void updateRoles() {
        Mockito.when(repository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(repository.save(user))
                .thenReturn(user);

        assertUserRoles(user, Role.ROLE_USER);

        User admin = service.updateRoles(userId, Set.of(Role.ROLE_ADMIN));

        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).save(user);

        assertUserRoles(admin, Role.ROLE_ADMIN);
        assertEquals(user.getId(), admin.getId());
    }

    @Test
    public void updateRolesThrowsException() {
        Mockito.when(repository.findById(userId))
                .thenReturn(Optional.empty());

        Exception e = assertThrows(NotFoundException.class, () ->
                service.updateRoles(userId, Set.of(Role.ROLE_ADMIN)));

        verify(repository, times(1)).findById(userId);
        verify(repository, times(0)).save(any(User.class));

        assertEquals("Error: could not find user with id " + userId, e.getMessage());
    }

    private void assertUserRoles(User user, Role role) {
        assertTrue(user.getRoles().contains(role));
        assertEquals(1, user.getRoles().size());
    }
}
