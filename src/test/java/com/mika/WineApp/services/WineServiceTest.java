package com.mika.WineApp.services;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.impl.WineServiceImpl;
import com.mika.WineApp.specifications.WineSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private WineRepository repository;

    @InjectMocks
    private WineServiceImpl service;

    @BeforeEach
    public void setupMocks() {
        this.wine = wines.stream().findAny().orElse(null);

        Mockito.lenient()
                .when(repository.findById(wine.getId()))
                .thenReturn(Optional.ofNullable(wine));

        Mockito.lenient()
                .when(repository.findById(nonExistingWineId))
                .thenReturn(Optional.empty());

        Mockito.lenient()
                .when(repository.save(wine))
                .thenReturn(wine);
    }

    @Test
    public void findAll() {
        var sortedWines = wines.stream()
                .sorted(Comparator.comparing(Wine::getName))
                .collect(Collectors.toList());

        Mockito.when(repository.findAllByOrderByNameAsc())
               .thenReturn(sortedWines);

        var foundWines = service.findAll();

        verify(repository, times(1)).findAllByOrderByNameAsc();
        assertEquals(sortedWines, foundWines);
    }

    @Test
    public void findById() {
        Wine foundWine = service.findById(wine.getId());
        verify(repository, times(1)).findById(wine.getId());
        assertEquals(wine, foundWine);
    }

    @Test
    public void findByNonExistingId() {
        Exception e = assertThrows(NotFoundException.class, () -> service.findById(nonExistingWineId));
        assertEquals(e.getMessage(), "Error: could not find wine with id " + nonExistingWineId);
        verify(repository, times(1)).findById(nonExistingWineId);
    }

    @Test
    public void addWine() {
        Mockito.when(securityUtils.getUsernameFromSecurityContext())
               .thenReturn(user.getUsername());

        Mockito.when(userService.findByUserName(user.getUsername()))
               .thenReturn(user);

        Mockito.when(repository.save(wine))
               .thenReturn(wine);

        Wine savedWine = service.add(wine);

        verify(repository, times(1)).existsByName(wine.getName());
        verify(securityUtils, times(1)).getUsernameFromSecurityContext();
        verify(userService, times(1)).findByUserName(user.getUsername());
        verify(repository, times(1)).save(wine);
        assertEquals(wine, savedWine);
    }

    @Test
    public void shouldNotAddSameWineTwice() {
        String name = wine.getName();

        Mockito.when(service.isValid(name))
               .thenReturn(true);

        Exception e = assertThrows(BadRequestException.class, () -> service.add(wine));
        assertEquals(e.getMessage(), "Error: wine with name " + name + " already exists!");
        verify(repository, times(1)).existsByName(name);
    }

    @Test
    public void editWine() {
        Wine editedWine = service.edit(wine.getId(), wine);
        verify(repository, times(1)).findById(wine.getId());
        verify(repository, times(1)).save(wine);
        assertEquals(wine, editedWine);
    }

    @Test
    public void editNonExistingWine() {
        Exception e = assertThrows(NotFoundException.class, () -> service.edit(nonExistingWineId, wine));
        assertEquals(e.getMessage(), "Error: could not find wine with id " + nonExistingWineId);
        verify(repository, times(1)).findById(nonExistingWineId);
        verify(repository, times(0)).save(wine);
    }

    @Test
    public void deleteWine() {
        service.delete(wine.getId());
        verify(repository, times(1)).deleteById(wine.getId());
    }

    @Test
    public void shouldThrowErrorWhenWrongUserTriesToDelete() {
        Mockito.when(securityUtils.getUsernameFromSecurityContext())
                .thenReturn(user.getUsername());

        Mockito.when(userService.findByUserName(user.getUsername()))
               .thenReturn(user);

        Mockito.doThrow(new BadRequestException(wine))
               .when(securityUtils).validateUpdateRequest(wine, user);

        Exception e = assertThrows(BadRequestException.class, () -> service.delete(wine.getId()));
        assertEquals("Error: tried to modify review or wine that you do not own!", e.getMessage());
    }

    @Test
    public void count() {
        Mockito.when(repository.count())
               .thenReturn((long) wines.size());

        long wineCount = service.count();
        verify(repository, times(1)).count();
        assertEquals(wines.size(), wineCount);
    }

    @Test
    public void findCountries() {
        var countries = wines.stream()
                .map(Wine::getCountry)
                .collect(Collectors.toList());

        Mockito.when(repository.findAllCountries())
               .thenReturn(countries);

        var foundCountries = service.findCountries();

        verify(repository, times(1)).findAllCountries();
        assertEquals(countries, foundCountries);
    }

    @Test
    public void findDescriptions() {
        var descriptions = wines.stream()
                .map(Wine::getDescription)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Mockito.when(repository.findAllDescriptions())
               .thenReturn(descriptions);

        var foundDescriptions = service.findDescriptions();

        verify(repository, times(1)).findAllDescriptions();
        assertEquals(descriptions, foundDescriptions);
    }

    @Test
    public void findFoodPairings() {
        var foodPairings = wines.stream()
                .map(Wine::getFoodPairings)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        Mockito.when(repository.findAllFoodPairings())
               .thenReturn(foodPairings);

        var foundFoodPairings = service.findFoodPairings();

        verify(repository, times(1)).findAllFoodPairings();
        assertEquals(foodPairings, foundFoodPairings);
    }

    @Test
    public void search() {
        Mockito.when(repository.findAll(any(WineSpecification.class)))
               .thenReturn(wines);

        var result = service.search("Viini", null, null, null, null);
        assertFalse(result.isEmpty());
    }

    @Test
    public void searchWithNullParameters() {
        var result = service.search(null, null, null, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void searchWithInvalidWineTypeThrowsException() {
        String wineType = "whit";
        Exception e = assertThrows(BadRequestException.class, () ->
                service.search(null, wineType, null, null, null));

        assertEquals("Error: requested wine type " + wineType + " does not exist.", e.getMessage());
    }
}