package com.mika.WineApp.wines;

import com.mika.WineApp.entities.Wine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WineSaverTest {

    @Mock
    private WineRepository repository;

    @InjectMocks
    private WineSaverImpl saver;

    @Test
    void isEmptyWhenNoWines() {
        when(repository.count()).thenReturn(0L);
        assertTrue(saver.isEmpty());
    }

    @Test
    void notEmptyWhenAtLeastOneWine() {
        when(repository.count()).thenReturn(1L);
        assertFalse(saver.isEmpty());
    }

    @Test
    void shouldSaveAllWines() {
        saver.saveAll(List.of(new Wine()));
        verify(repository, atLeastOnce()).saveAll(anyCollection());
    }
}
