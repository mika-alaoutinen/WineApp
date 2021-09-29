package com.mika.WineApp.controllers;

import com.mika.WineApp.services.WineService;
import com.mika.api.WinesInfoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WineInfoController implements WinesInfoApi {
    private final WineService service;

    @Override
    public ResponseEntity<List<String>> findCountries() {
        return ResponseEntity.ok(service.findCountries());
    }

    @Override
    public ResponseEntity<List<String>> findDescriptions() {
        return ResponseEntity.ok(service.findDescriptions());
    }

    @Override
    public ResponseEntity<List<String>> findFoodPairings() {
        return ResponseEntity.ok(service.findFoodPairings());
    }

    @Override
    public ResponseEntity<Boolean> validateWineName(String name) {
        return ResponseEntity.ok(service.isValid(name));
    }

    @Override
    public ResponseEntity<Integer> wineCount() {
        return ResponseEntity.ok(service.count());
    }
}
