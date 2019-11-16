package com.mika.WineApp.services;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class WineServiceTest {

    @Mock
    private WineRepository repository;

    @InjectMocks
    private WineService service;

    @BeforeEach
    void setUp() {
        var description = List.of("puolikuiva", "sitruunainen", "yrttinen");
        var foodPairings = List.of("kala", "kasvisruoka", "seurustelujuoma");

        Wine w1 = new Wine("Valkoviini", WineType.WHITE, "Espanja", 8.75, 0.75, description, foodPairings, "invalid");
        w1.setId(11L);
        Wine w2 = new Wine("Punaviini", WineType.RED, "Ranska", 29.95, 3.0, description, foodPairings, "invalid");
        w2.setId(12L);

        List.of(w1, w2).forEach(service::add);
    }

    @Test
    void findAll() {
    }
//
//    @Test
//    void findByName() {
//    }
//
//    @Test
//    void findByType() {
//    }
//
//    @Test
//    void findByCountry() {
//    }
//
//    @Test
//    void findByQuantity() {
//    }
//
//    @Test
//    void findByPrice() {
//    }
//
//    @Test
//    void findByDescription() {
//    }
//
//    @Test
//    void findByFoodPairings() {
//    }
//
//    @Test
//    void findById() {
//    }
//
//    @Test
//    void find() {
//    }
//
//    @Test
//    void add() {
//    }
//
//    @Test
//    void edit() {
//    }
//
//    @Test
//    void delete() {
//    }
}