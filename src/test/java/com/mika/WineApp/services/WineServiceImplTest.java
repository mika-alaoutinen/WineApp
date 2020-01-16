package com.mika.WineApp.services;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class WineServiceImplTest {

    @Mock
    private WineRepository repository;

    @InjectMocks
    private WineServiceImpl service;

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
}