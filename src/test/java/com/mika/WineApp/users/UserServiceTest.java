package com.mika.WineApp.users;

import com.mika.WineApp.entities.Role;
import com.mika.WineApp.entities.User;
import com.mika.WineApp.errors.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    UserService service;

    @Test
    void shouldFindAll() {
        service.findAll();
        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    void shouldFindById() {
        service.findById(1L);
        verify(repository, atLeastOnce()).findById(1L);
    }

    @Test
    void shouldFindByUserName() {
        service.findByUsername("username");
        verify(repository, atLeastOnce()).findByUsername("username");
    }

    @Test
    void shouldSaveUserIfNotExists() {
        when(repository.existsByUsername(anyString())).thenReturn(false);
        service.save(new User("username", "password"));
        verify(repository, atLeastOnce()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        when(repository.existsByUsername(anyString())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> service.save(new User("username", "password")));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldUpdateRolesIfUserExists() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(repository.save(any(User.class))).thenReturn(new User());
        assertFalse(service
                .updateRoles(1L, Set.of(Role.ROLE_ADMIN))
                .isEmpty());
        verify(repository, atLeastOnce()).findById(1L);
        verify(repository, atLeastOnce()).save(any(User.class));
    }

    @Test
    void shouldNotUpdateUserWhenWrongUserId() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(service
                .updateRoles(1L, Set.of())
                .isEmpty());
        verify(repository, atLeastOnce()).findById(1L);
        verify(repository, never()).save(any((User.class)));
    }
}
