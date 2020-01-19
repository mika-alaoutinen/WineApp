package com.mika.WineApp.services;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class WineServiceTest {

    @Mock
    private WineRepository repository;

    @InjectMocks
    private WineServiceImpl service;

    private final List<Wine> wines = TestData.initWines();
    private Wine wine;

    @BeforeEach
    void initWine() {
        this.wine = wines.stream()
                .findAny()
                .orElse(null);
    }

    @Test
    public void findAll() {
        var sortedWines = wines.stream()
                .sorted(Comparator.comparing(Wine::getName))
                .collect(Collectors.toList());

        Mockito.when(repository.findAllByOrderByNameAsc())
               .thenReturn(sortedWines);

        var foundWines = service.findAll();

        Mockito.verify(repository, Mockito.times(1)).findAllByOrderByNameAsc();
        Assertions.assertEquals(sortedWines, foundWines);
    }

    @Test
    public void findById() {
        long id = wine.getId();

        Mockito.when(repository.findById(id))
               .thenReturn(Optional.ofNullable(wine));

        Wine foundWine = service.findById(id).orElse(null);

        Mockito.verify(repository, Mockito.times(1)).findById(id);
        Assertions.assertEquals(wine, foundWine);
    }

    @Test
    public void addWine() {
        Mockito.when(repository.save(wine))
               .thenReturn(wine);

        Wine addedWine = service.add(wine);

        Mockito.verify(repository, Mockito.times(1)).save(wine);
        Assertions.assertEquals(wine, addedWine);
    }

    @Test
    public void editWine() {
        long id = wine.getId();

        Mockito.when(repository.findById(id))
               .thenReturn(Optional.ofNullable(wine));

        Mockito.when(repository.save(wine))
               .thenReturn((wine));

        Wine editedWine = service.edit(id, wine);

        Mockito.verify(repository, Mockito.times(1)).findById(id);
        Mockito.verify(repository, Mockito.times(1)).save(wine);
        Assertions.assertEquals(wine, editedWine);
    }

    @Test
    public void deleteWine() {
        long id = wine.getId();
        service.delete(id);

        Mockito.verify(repository, Mockito.times(1))
               .deleteById(id);
    }

    @Test
    public void count() {
        Mockito.when(repository.count())
               .thenReturn((long) wines.size());

        long wineCount = service.count();

        Mockito.verify(repository, Mockito.times(1)).count();
        Assertions.assertEquals(wines.size(), wineCount);
    }

    @Test
    public void findCountries() {
        var countries = wines.stream()
                .map(Wine::getCountry)
                .collect(Collectors.toList());

        Mockito.when(repository.findAllCountries()).thenReturn(countries);

        var foundCountries = service.findCountries();

        Mockito.verify(repository, Mockito.times(1)).findAllCountries();
        Assertions.assertEquals(countries, foundCountries);
    }

    @Test
    public void findDescriptions() {
        var descriptions = wines.stream()
                .map(Wine::getDescription)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Mockito.when(repository.findAllDescriptions()).thenReturn(descriptions);

        var foundDescriptions = service.findDescriptions();

        Mockito.verify(repository, Mockito.times(1)).findAllDescriptions();
        Assertions.assertEquals(descriptions, foundDescriptions);
    }

    @Test
    public void findFoodPairings() {
        var foodPairings = wines.stream()
                .map(Wine::getFoodPairings)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Mockito.when(repository.findAllFoodPairings()).thenReturn(foodPairings);

        var foundFoodPairings = service.findFoodPairings();

        Mockito.verify(repository, Mockito.times(1)).findAllFoodPairings();
        Assertions.assertEquals(foodPairings, foundFoodPairings);
    }

    @Test
    public void searchWithNullParameters() {
        var result = service.search(null, null, null, null, null);
        Assertions.assertTrue(result.isEmpty());
    }
}