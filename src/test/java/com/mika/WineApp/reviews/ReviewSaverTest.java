package com.mika.WineApp.reviews;

import com.mika.WineApp.entities.Review;
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
class ReviewSaverTest {
    
    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewSaverImpl saver;

    @Test
    void isEmptyWhenNoReviews() {
        when(repository.count()).thenReturn(0L);
        assertTrue(saver.isEmpty());
    }

    @Test
    void notEmptyWhenAtLeastOneReview() {
        when(repository.count()).thenReturn(1L);
        assertFalse(saver.isEmpty());
    }

    @Test
    void shouldSaveAllReviews() {
        saver.saveAll(List.of(new Review()));
        verify(repository, atLeastOnce()).saveAll(anyCollection());
    }
}
