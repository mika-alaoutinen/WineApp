package com.mika.WineApp.services;

import com.mika.WineApp.errors.InvalidWineTypeException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class WineServiceTest {

    @Mock private WineRepository repository;
    @InjectMocks private WineService service;

    private List<Wine> wineList;

    @BeforeEach
    void setUp() {
        var description1 = List.of("puolikuiva", "sitruunainen", "yrttinen");
        var description2 = List.of("Kuiva", "sitruunainen", "pirskahteleva");
        var description3 = List.of("tanniininen", "mokkainen", "täyteläinen", "tamminen");

        var foodPairings1 = List.of("kala", "kasvisruoka", "seurustelujuoma");
        var foodPairings2 = List.of("kana", "kasvisruoka", "noutopöytä");
        var foodPairings3 = List.of("nauta", "pataruuat", "noutopöytä");

        Wine white1 = new Wine("Valkoviini 1", WineType.WHITE, "Espanja", 8.75, 0.75, description1, foodPairings1, "invalid");
        Wine white2 = new Wine("Valkoviini 2", WineType.WHITE, "Espanja", 8.75, 0.75, description2, foodPairings2, "invalid");
        Wine red1 = new Wine("Punaviini", WineType.RED, "Ranska", 29.95, 3.0, description3, foodPairings3, "invalid");
        Wine red2 = new Wine("Gato Negra", WineType.RED, "Italia", 30.95, 3.0, description3, foodPairings3, "invalid");

        white1.setId(11L);

        wineList = List.of(white1, white2, red1, red2);
    }

    @Test
    void findByExistingWineType() {
        Mockito.when(repository.findDistinctByType(WineType.RED))
               .thenReturn(wineList.subList(1, 3));

        assertEquals(2, service.findByType("red").size());
    }

    @Test()
    void findByNonExistingWineType() {
        assertThrows(InvalidWineTypeException.class, () -> service.findByType("grey"));
    }

    @Test
    void findByDescription1() {
        var findDesc = List.of("sitruunainen");

        Mockito.when(repository.findDistinctByDescriptionInIgnoreCase(findDesc))
                .thenReturn(wineList);

        assertEquals(2, service.findByDescription(findDesc).size());
    }

    @Test
    void findByDescription2() {
        var findDesc = List.of("puolikuiva", "sitruunainen");

        Mockito.when(repository.findDistinctByDescriptionInIgnoreCase(findDesc))
               .thenReturn(wineList);

        assertEquals(1, service.findByDescription(findDesc).size());
    }

    @Test
    void findByFoodPairings1() {
        var foodPairings = List.of("kasvisruoka");

        Mockito.when(repository.findDistinctByFoodPairingsInIgnoreCase(foodPairings))
               .thenReturn(wineList);

        assertEquals(2, service.findByFoodPairings(foodPairings).size());
    }

    @Test
    void findByFoodPairings2() {
        var foodPairings = List.of("kasvisruoka", "noutopöytä");

        Mockito.when(repository.findDistinctByFoodPairingsInIgnoreCase(foodPairings))
               .thenReturn(wineList);

        assertEquals(1, service.findByFoodPairings(foodPairings).size());
    }
}