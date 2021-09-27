package com.mika.WineApp.controllers;

import api.WinesSearchApi;
import com.mika.WineApp.mappers.WineMapper;
import com.mika.WineApp.services.WineService;
import lombok.RequiredArgsConstructor;
import model.WineDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WinesSearchController implements WinesSearchApi {
    private final WineMapper mapper;
    private final WineService service;

    @Override
    public ResponseEntity<List<WineDTO>> search(String name, String type, List<String> countries, List<Double> volumes, List<Double> priceRange) {
        var wines = service
                .search(name, type, countries, volumes, priceRange)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(wines);
    }
}
