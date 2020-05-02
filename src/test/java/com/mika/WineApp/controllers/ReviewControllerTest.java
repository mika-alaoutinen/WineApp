package com.mika.WineApp.controllers;

import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.services.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {
    private static final Long id = 1L;
    private static final Review review = new Review();

    @Mock
    private ReviewService service;

    @InjectMocks
    private ReviewController controller;

    @Test
    public void findAll() {
        controller.findAll();
        verify(service, times(1)).findAll();
    }

    @Test
    public void findById() {
        controller.findById(id);
        verify(service, times(1)).findById(id);
    }

    @Test
    public void findByWineId() {
        controller.findByWineId(id);
        verify(service, times(1)).findByWineId(id);
    }

    @Test
    public void findByWineName() {
        controller.findByWineName("Herkkuviini");
        verify(service, times(1)).findByWineName("Herkkuviini");
    }

    @Test
    public void add() {
        controller.add(id, review);
        verify(service, times(1)).add(id, review);
    }

    @Test
    public void edit() {
        controller.edit(id, review);
        verify(service, times(1)).edit(id, review);
    }

    @Test
    public void delete() {
        controller.delete(id);
        verify(service, times(1)).delete(id);
    }

    @Test
    public void count() {
        controller.count();
        verify(service, times(1)).count();
    }

    @Test
    public void search() {
        String author = "author";
        String[] dateRange = new String[2];
        Double[] ratingRange = new Double[2];
        controller.search(author, dateRange, ratingRange);
        verify(service, times(1)).search(author, dateRange, ratingRange);
    }

    @Test
    public void findNewest() {
        controller.findNewest(5);
        verify(service, times(1)).findNewest(5);
    }

    @Test
    public void findBestRated() {
        controller.findBest(5);
        verify(service, times(1)).findBestRated(5);
    }

    @Test
    public void findWorstRated() {
        controller.findWorst(5);
        verify(service, times(1)).findWorstRated(5);
    }
}
