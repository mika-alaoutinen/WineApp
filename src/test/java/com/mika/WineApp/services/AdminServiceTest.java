package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.errors.BadRequestException;
import com.mika.WineApp.errors.NotFoundException;
import com.mika.WineApp.services.impl.AdminServiceImpl;
import com.mika.WineApp.users.UserRepository;
import com.mika.WineApp.users.model.Role;
import com.mika.WineApp.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    private final static List<User> users = TestData.initTestUsers();
    private final static User user = users.get(0);
    private final static Long userId = user.getId();
    private final static Long nonExistentUserId = 123L;
    private final static String username = user.getUsername();

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AdminServiceImpl service;

    @BeforeEach
    void setupMocks() {
        Mockito
                .lenient()
                .when(repository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito
                .lenient()
                .when(repository.findById(nonExistentUserId))
                .thenReturn(Optional.empty());

        Mockito
                .lenient()
                .when(repository.findByUsername(username))
                .thenReturn(Optional.of(user));

        Mockito
                .lenient()
                .when(repository.save(user))
                .thenReturn(user);
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(users);
        var foundUsers = service.findAll();
        verify(repository, times(1)).findAll();
        assertEquals(users, foundUsers);
    }

    @Test
    void findById() {
        User foundUser = service.findById(userId);
        verify(repository, times(1)).findById(userId);
        assertEquals(foundUser, user);
    }

    @Test
    void findByIdThrowsException() {
        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.findById(nonExistentUserId));

        verify(repository, times(1)).findById(nonExistentUserId);
        assertEquals("Could not find user with id " + nonExistentUserId, e.getMessage());
    }

    @Test
    void findByUsername() {
        assertEquals(user, service.findByUserName(username));
        verify(repository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsernameThrowsException() {
        String nonExistentName = "nonexistent";

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.findByUserName(nonExistentName));

        verify(repository, times(1)).findByUsername(nonExistentName);
        assertEquals("Could not find user with username " + nonExistentName, e.getMessage());
    }

    @Test
    void save() {
        when(repository.existsByUsername(username)).thenReturn(false);
        User savedUser = service.save(user);
        verify(repository, times(1)).existsByUsername(username);
        verify(repository, times(1)).save(user);
        assertEquals(savedUser, user);
    }

    @Test
    void throwsExceptionIfUserNameIsTaken() {
        when(repository.existsByUsername(username)).thenReturn(true);
        BadRequestException e = assertThrows(BadRequestException.class, () -> service.save(user));
        assertEquals("Username " + username + " already exists!", e.getMessage());
        verify(repository, times(1)).existsByUsername(username);
        verify(repository, times(0)).save(any(User.class));
    }

    @Test
    void updateRoles() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        assertUserRoles(user, Role.ROLE_USER);

        User admin = service.updateRoles(userId, Set.of(Role.ROLE_ADMIN));

        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).save(user);
        assertUserRoles(admin, Role.ROLE_ADMIN);
        assertEquals(user.getId(), admin.getId());
    }

    @Test
    void updateRolesThrowsException() {
        Exception e = assertThrows(NotFoundException.class, () ->
                service.updateRoles(nonExistentUserId, Set.of(Role.ROLE_ADMIN)));

        verify(repository, times(1)).findById(nonExistentUserId);
        verify(repository, times(0)).save(any(User.class));
        assertEquals("Could not find user with id " + nonExistentUserId, e.getMessage());
    }

    private void assertUserRoles(User user, Role role) {
        assertTrue(user
                .getRoles()
                .contains(role));

        assertEquals(1, user
                .getRoles()
                .size());
    }
}
