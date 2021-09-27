package com.mika.WineApp.controllers;

import api.WinesInfoApi;
import com.mika.WineApp.services.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WinesInfoController implements WinesInfoApi {
    private final WineService service;

    @Override
    public ResponseEntity<Integer> count() {
        return ResponseEntity.ok(service.count());
    }

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
    public ResponseEntity<Boolean> validateName(String name) {
        return ResponseEntity.ok(service.isValid(name));
    }
}
