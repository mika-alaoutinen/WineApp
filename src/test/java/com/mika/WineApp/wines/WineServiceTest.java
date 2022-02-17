package com.mika.WineApp.wines;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.authentication.UserAuthentication;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.entities.WineType;
import com.mika.WineApp.errors.BadRequestException;
import com.mika.WineApp.errors.ForbiddenException;
import com.mika.WineApp.search.WineSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WineServiceTest {
    private static final List<Wine> wines = TestData.initWines();

    @Mock
    private UserAuthentication userAuth;

    @Mock
    private WineRepository repository;

    @InjectMocks
    private WineServiceImpl service;

    @Test
    void findAll() {
        var sortedWines = wines
                .stream()
                .sorted(Comparator.comparing(Wine::getName))
                .collect(Collectors.toList());

        when(repository.findAllByOrderByNameAsc()).thenReturn(sortedWines);
        var foundWines = service.findAll();

        verify(repository, times(1)).findAllByOrderByNameAsc();
        assertEquals(sortedWines, foundWines);
    }

    @Test
    void findById() {
        service.findById(1L);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findByNonExistingId() {
        long nonExistingWineId = 2L;
        assertTrue(service
                .findById(nonExistingWineId)
                .isEmpty());
        verify(repository, times(1)).findById(nonExistingWineId);
    }

    @Test
    void addWine() {
        Wine wine = Wine
                .builder()
                .name("name")
                .build();
        when(userAuth.setUser(any(Wine.class))).thenReturn(wine);
        when(repository.save(any(Wine.class))).thenReturn(wine);

        service.add(wine);
        verify(repository, times(1)).existsByName(anyString());
        verify(userAuth, times(1)).setUser(any(Wine.class));
        verify(repository, times(1)).save(any(Wine.class));
    }

    @Test
    void shouldNotAddSameWineTwice() {
        Wine wine = Wine
                .builder()
                .name("wine name")
                .build();
        when(service.isValid(anyString())).thenReturn(true);

        BadRequestException e = assertThrows(BadRequestException.class, () -> service.add(wine));
        assertEquals(e.getMessage(), "Wine with name wine name already exists!");
        verify(repository, times(1)).existsByName(anyString());
    }

    @Test
    void editWine() {
        Wine wine = Wine
                .builder()
                .name("wine name")
                .type(WineType.WHITE)
                .country("country")
                .price(10)
                .volume(1)
                .description(Collections.emptyList())
                .foodPairings(Collections.emptyList())
                .url("url")
                .build();

        when(repository.findById(anyLong())).thenReturn(Optional.of(wine));
        when(userAuth.isUserAllowedToEdit(any(Wine.class))).thenReturn(true);
        when(repository.save(any(Wine.class))).thenReturn(wine);

        service.edit(1L, wine);
        verify(repository, times(1)).findById(1L);
        verify(userAuth, times(1)).isUserAllowedToEdit(any(Wine.class));
        verify(repository, times(1)).save(any(Wine.class));
    }

    @Test
    void editNonExistingWine() {
        assertTrue(service
                .edit(13L, new Wine())
                .isEmpty());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, never()).save(any(Wine.class));
    }

    @Test
    void editWineWithoutPermission() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Wine()));
        ForbiddenException e = assertThrows(ForbiddenException.class, () ->
                service.edit(1L, new Wine()));

        assertEquals("Tried to modify a wine that you do not own!", e.getMessage());
        verify(repository, times(1)).findById(anyLong());
        verify(userAuth, times(1)).isUserAllowedToEdit(any(Wine.class));
        verify(repository, never()).save(any(Wine.class));
    }

    @Test
    void deleteWine() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Wine()));
        when(userAuth.isUserAllowedToEdit(any(Wine.class))).thenReturn(true);
        service.delete(1L);
        verify(userAuth, times(1)).isUserAllowedToEdit(any(Wine.class));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowErrorWhenWrongUserTriesToDelete() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Wine()));
        ForbiddenException e = assertThrows(ForbiddenException.class, () -> service.delete(1L));
        assertEquals("Tried to modify a wine that you do not own!", e.getMessage());
    }

    @Test
    void count() {
        when(repository.count()).thenReturn((long) wines.size());
        long wineCount = service.count();
        verify(repository, times(1)).count();
        assertEquals(wines.size(), wineCount);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isAllowedToEditTrue(boolean isAllowed) {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Wine()));
        when(userAuth.isUserAllowedToEdit(any(Wine.class))).thenReturn(isAllowed);
        assertEquals(service.isAllowedToEdit(1L), isAllowed);
        verify(repository, times(1)).findById(anyLong());
        verify(userAuth, times(1)).isUserAllowedToEdit(any(Wine.class));
    }

    @Test
    void findCountries() {
        var countries = wines
                .stream()
                .map(Wine::getCountry)
                .collect(Collectors.toList());

        when(repository.findAllCountries()).thenReturn(countries);
        var foundCountries = service.findCountries();

        verify(repository, times(1)).findAllCountries();
        assertEquals(countries, foundCountries);
    }

    @Test
    void findDescriptions() {
        var descriptions = wines
                .stream()
                .map(Wine::getDescription)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        when(repository.findAllDescriptions()).thenReturn(descriptions);
        var foundDescriptions = service.findDescriptions();

        verify(repository, times(1)).findAllDescriptions();
        assertEquals(descriptions, foundDescriptions);
    }

    @Test
    void findFoodPairings() {
        var foodPairings = wines
                .stream()
                .map(Wine::getFoodPairings)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        when(repository.findAllFoodPairings()).thenReturn(foodPairings);
        var foundFoodPairings = service.findFoodPairings();

        verify(repository, times(1)).findAllFoodPairings();
        assertEquals(foodPairings, foundFoodPairings);
    }

    @Test
    void search() {
        when(repository.findAll(any(WineSpecification.class))).thenReturn(wines);
        var result = service.search("Viini", null, null, null, null);
        assertFalse(result.isEmpty());
    }

    @Test
    void emptySearch() {
        var result = service.search(null, null, null, null, null);
        assertTrue(result.isEmpty());
    }
}