package com.mika.WineApp.controllers;

import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.services.WineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WineControllerTest {
    private static final Long id = 1L;
    private static final Wine wine = new Wine();

    @Mock
    private WineService service;

    @InjectMocks
    private WineController controller;

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
    public void add() {
        controller.add(wine);
        verify(service, times(1)).add(wine);
    }

    @Test
    public void edit() {
        controller.edit(id, wine);
        verify(service, times(1)).edit(id, wine);
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
    public void countries() {
        controller.countries();
        verify(service, times(1)).findCountries();
    }

    @Test
    public void descriptions() {
        controller.descriptions();
        verify(service, times(1)).findDescriptions();
    }

    @Test
    public void foodPairings() {
        controller.foodPairings();
        verify(service, times(1)).findFoodPairings();
    }

    @Test
    public void search() {
        String name = "Name";
        String type = "RED";
        var countries = List.of("Espanja");
        var volumes = List.of(2.0);
        Integer[] priceRange = { 10, 20 };
        controller.search(name, type, countries, volumes, priceRange);
        verify(service, times(1)).search(name, type, countries, volumes, priceRange);
    }

    @Test
    public void validate() {
        controller.validate("Mika");
        verify(service, times(1)).isValid("Mika");
    }
}
