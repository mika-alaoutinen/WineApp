package com.mika.WineApp.services;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.services.impl.WineServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WineServiceTest extends ServiceTest {

    @InjectMocks
    private WineServiceImpl service;

    @Test
    public void findAll() {
        var sortedWines = wines.stream()
                .sorted(Comparator.comparing(Wine::getName))
                .collect(Collectors.toList());

        Mockito.when(wineRepository.findAllByOrderByNameAsc())
               .thenReturn(sortedWines);

        var foundWines = service.findAll();

        Mockito.verify(wineRepository, Mockito.times(1)).findAllByOrderByNameAsc();
        assertEquals(sortedWines, foundWines);
    }

    @Test
    public void findById() {
        long id = wine.getId();

        Mockito.when(wineRepository.findById(id))
               .thenReturn(Optional.ofNullable(wine));

        Wine foundWine = service.findById(id);

        Mockito.verify(wineRepository, Mockito.times(1)).findById(id);
        assertEquals(wine, foundWine);
    }

    @Test
    public void addWine() {
        Mockito.when(wineRepository.save(wine))
               .thenReturn(wine);

        Wine addedWine = service.add(wine);

        Mockito.verify(wineRepository, Mockito.times(1)).save(wine);
        assertEquals(wine, addedWine);
    }

    @Test
    public void shouldNotAddSameWineTwice() {
        String name = wine.getName();

        Mockito.when(service.isValid(name))
               .thenReturn(true);

        Exception e = assertThrows(BadRequestException.class, () -> service.add(wine));
        assertEquals(e.getMessage(), "Error: wine with name " + name + " already exists!");
        Mockito.verify(wineRepository, Mockito.times(1)).existsByName(name);
    }

    @Test
    public void editWine() {
        long id = wine.getId();

        Mockito.when(wineRepository.findById(id))
               .thenReturn(Optional.ofNullable(wine));

        Mockito.when(wineRepository.save(wine))
               .thenReturn((wine));

        Wine editedWine = service.edit(id, wine);

        Mockito.verify(wineRepository, Mockito.times(1)).findById(id);
        Mockito.verify(wineRepository, Mockito.times(1)).save(wine);
        assertEquals(wine, editedWine);
    }

    @Test
    public void deleteWine() {
        long id = wine.getId();
        service.delete(id);

        Mockito.verify(wineRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void count() {
        Mockito.when(wineRepository.count())
               .thenReturn((long) wines.size());

        long wineCount = service.count();

        Mockito.verify(wineRepository, Mockito.times(1)).count();
        assertEquals(wines.size(), wineCount);
    }

    @Test
    public void findCountries() {
        var countries = wines.stream()
                .map(Wine::getCountry)
                .collect(Collectors.toList());

        Mockito.when(wineRepository.findAllCountries()).thenReturn(countries);

        var foundCountries = service.findCountries();

        Mockito.verify(wineRepository, Mockito.times(1)).findAllCountries();
        assertEquals(countries, foundCountries);
    }

    @Test
    public void findDescriptions() {
        var descriptions = wines.stream()
                .map(Wine::getDescription)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Mockito.when(wineRepository.findAllDescriptions()).thenReturn(descriptions);

        var foundDescriptions = service.findDescriptions();

        Mockito.verify(wineRepository, Mockito.times(1)).findAllDescriptions();
        assertEquals(descriptions, foundDescriptions);
    }

    @Test
    public void findFoodPairings() {
        var foodPairings = wines.stream()
                .map(Wine::getFoodPairings)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Mockito.when(wineRepository.findAllFoodPairings()).thenReturn(foodPairings);

        var foundFoodPairings = service.findFoodPairings();

        Mockito.verify(wineRepository, Mockito.times(1)).findAllFoodPairings();
        assertEquals(foodPairings, foundFoodPairings);
    }

    @Test
    public void searchWithNullParameters() {
        var result = service.search(null, null, null, null, null);
        assertTrue(result.isEmpty());
    }
}