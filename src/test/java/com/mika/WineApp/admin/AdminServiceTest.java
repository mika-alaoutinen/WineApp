package com.mika.WineApp.admin;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.entities.Role;
import com.mika.WineApp.entities.User;
import com.mika.WineApp.users.UserReader;
import com.mika.WineApp.users.UserWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserReader reader;

    @Mock
    private UserWriter writer;

    @InjectMocks
    private AdminServiceImpl service;

    @Test
    void findAll() {
        when(reader.findAll()).thenReturn(TestData.initTestUsers());
        assertEquals(2, service
                .findAll()
                .size());
        verify(reader, times(1)).findAll();
    }

    @Test
    void findById() {
        when(reader.findById(anyLong())).thenReturn(Optional.of(new User()));
        assertTrue(service
                .findById(1L)
                .isPresent());
        verify(reader, times(1)).findById(1L);
    }

    @Test
    void findByIdReturnEmpty() {
        assertTrue(service
                .findById(1L)
                .isEmpty());
    }

    @Test
    void findByUsername() {
        when(reader.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        assertTrue(service
                .findByUsername("test_user")
                .isPresent());
        verify(reader, times(1)).findByUsername("test_user");
    }

    @Test
    void findByUsernameReturnsEmpty() {
        assertTrue(service
                .findByUsername("not a user")
                .isEmpty());
        verify(reader, times(1)).findByUsername("not a user");
    }

    @Test
    void updateRolesCallsWriter() {
        service.updateRoles(1L, Set.of(Role.ROLE_ADMIN));
        verify(writer, times(1)).updateRoles(1L, Set.of(Role.ROLE_ADMIN));
    }
}
