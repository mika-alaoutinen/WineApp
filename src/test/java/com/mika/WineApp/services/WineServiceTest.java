package com.mika.WineApp.services;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.forbidden.ForbiddenException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.services.impl.WineServiceImpl;
import com.mika.WineApp.specifications.WineSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class WineServiceTest extends ServiceTest {

    @InjectMocks
    private WineServiceImpl service;

    @BeforeEach
    void setupMocks() {
        this.wine = wines.stream().findAny().orElse(null);

        Mockito.lenient()
                .when(wineRepository.findById(wine.getId()))
                .thenReturn(Optional.ofNullable(wine));

        Mockito.lenient()
                .when(wineRepository.findById(nonExistingWineId))
                .thenReturn(Optional.empty());

        Mockito.lenient()
                .when(wineRepository.save(wine))
                .thenReturn(wine);
    }

    @Test
    void findAll() {
        var sortedWines = wines.stream()
                .sorted(Comparator.comparing(Wine::getName))
                .collect(Collectors.toList());

        Mockito.when(wineRepository.findAllByOrderByNameAsc())
               .thenReturn(sortedWines);

        var foundWines = service.findAll();

        verify(wineRepository, times(1)).findAllByOrderByNameAsc();
        assertEquals(sortedWines, foundWines);
    }

    @Test
    void findById() {
        Wine foundWine = service.findById(wine.getId());
        verify(wineRepository, times(1)).findById(wine.getId());
        assertEquals(wine, foundWine);
    }

    @Test
    void findByNonExistingId() {
        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.findById(nonExistingWineId));

        assertEquals(e.getMessage(), "Error: could not find wine with id " + nonExistingWineId);
        verify(wineRepository, times(1)).findById(nonExistingWineId);
    }

    @Test
    void addWine() {
        Mockito.when(userService.setUser(wine))
               .thenReturn(wine);

        Mockito.when(wineRepository.save(wine))
               .thenReturn(wine);

        Wine savedWine = service.add(wine);

        verify(wineRepository, times(1)).existsByName(wine.getName());
        verify(userService, times(1)).setUser(wine);
        verify(wineRepository, times(1)).save(wine);
        assertEquals(wine, savedWine);
    }

    @Test
    void shouldNotAddSameWineTwice() {
        String name = wine.getName();

        Mockito.when(service.isValid(name))
               .thenReturn(true);

        BadRequestException e = assertThrows(BadRequestException.class, () ->
                service.add(wine));

        assertEquals(e.getMessage(), "Error: wine with name " + name + " already exists!");
        verify(wineRepository, times(1)).existsByName(name);
    }

    @Test
    void editWine() {
        Mockito.when(userService.isUserAllowedToEdit(wine))
               .thenReturn(true);

        Wine editedWine = service.edit(wine.getId(), wine);
        verify(wineRepository, times(1)).findById(wine.getId());
        verify(userService, times(1)).isUserAllowedToEdit(wine);
        verify(wineRepository, times(1)).save(wine);
        assertEquals(wine, editedWine);
    }

    @Test
    void editNonExistingWine() {
        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.edit(nonExistingWineId, wine));

        assertEquals(e.getMessage(), "Error: could not find wine with id " + nonExistingWineId);
        verify(wineRepository, times(1)).findById(nonExistingWineId);
        verify(wineRepository, times(0)).save(wine);
    }

    @Test
    void editWineWithoutPermission() {
        ForbiddenException e = assertThrows(ForbiddenException.class, () ->
                service.edit(wine.getId(), wine));

        assertEquals("Error: tried to modify a wine that you do not own!", e.getMessage());
        verify(wineRepository, times(1)).findById(wine.getId());
        verify(userService, times(1)).isUserAllowedToEdit(wine);
        verify(wineRepository, times(0)).save(any(Wine.class));
    }

    @Test
    void deleteWine() {
        Mockito.when(userService.isUserAllowedToEdit(wine))
               .thenReturn(true);

        service.delete(wine.getId());
        verify(userService, times(1)).isUserAllowedToEdit(wine);
        verify(wineRepository, times(1)).deleteById(wine.getId());
    }

    @Test
    void shouldThrowErrorWhenWrongUserTriesToDelete() {
        ForbiddenException e = assertThrows(ForbiddenException.class, () ->
                service.delete(wine.getId()));

        assertEquals("Error: tried to modify a wine that you do not own!", e.getMessage());
    }

    @Test
    void count() {
        Mockito.when(wineRepository.count())
               .thenReturn((long) wines.size());

        long wineCount = service.count();
        verify(wineRepository, times(1)).count();
        assertEquals(wines.size(), wineCount);
    }

    @Test
    void isAllowedToEditTrue() {
        isAllowedToEdit(true);
    }

    @Test
    void isAllowedToEditFalse() {
        isAllowedToEdit(false);
    }

    @Test
    void findCountries() {
        var countries = wines.stream()
                .map(Wine::getCountry)
                .collect(Collectors.toList());

        Mockito.when(wineRepository.findAllCountries())
               .thenReturn(countries);

        var foundCountries = service.findCountries();

        verify(wineRepository, times(1)).findAllCountries();
        assertEquals(countries, foundCountries);
    }

    @Test
    void findDescriptions() {
        var descriptions = wines.stream()
                .map(Wine::getDescription)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Mockito.when(wineRepository.findAllDescriptions())
               .thenReturn(descriptions);

        var foundDescriptions = service.findDescriptions();

        verify(wineRepository, times(1)).findAllDescriptions();
        assertEquals(descriptions, foundDescriptions);
    }

    @Test
    void findFoodPairings() {
        var foodPairings = wines.stream()
                .map(Wine::getFoodPairings)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Mockito.when(wineRepository.findAllFoodPairings())
               .thenReturn(foodPairings);

        var foundFoodPairings = service.findFoodPairings();

        verify(wineRepository, times(1)).findAllFoodPairings();
        assertEquals(foodPairings, foundFoodPairings);
    }

    @Test
    void search() {
        Mockito.when(wineRepository.findAll(any(WineSpecification.class)))
               .thenReturn(wines);

        var result = service.search("Viini", null, null, null, null);
        assertFalse(result.isEmpty());
    }

    @Test
    void searchWithNullParameters() {
        var result = service.search(null, null, null, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchWithInvalidWineTypeThrowsException() {
        String wineType = "whit";
        BadRequestException e = assertThrows(BadRequestException.class, () ->
                service.search(null, wineType, null, null, null));

        assertEquals("Error: requested wine type " + wineType + " does not exist.", e.getMessage());
    }

    private void isAllowedToEdit(boolean isAllowed) {
        Mockito.when(userService.isUserAllowedToEdit(wine))
               .thenReturn(isAllowed);

        assertEquals(service.isAllowedToEdit(wine.getId()), isAllowed);
        verify(wineRepository, times(1)).findById(wine.getId());
        verify(userService, times(1)).isUserAllowedToEdit(wine);
    }
}