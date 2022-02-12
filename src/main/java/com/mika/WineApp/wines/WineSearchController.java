package com.mika.WineApp.wines;

import com.mika.WineApp.mappers.WineMapper;
import com.mika.WineApp.services.WineService;
import com.mika.api.WinesSearchApi;
import com.mika.model.WineDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
class WineSearchController implements WinesSearchApi {
    private final WineMapper mapper;
    private final WineService service;

    @Override
    public ResponseEntity<List<WineDTO>> wineSearch(
            String name,
            String type,
            List<String> countries,
            List<Double> volumes,
            List<Double> priceRange
    ) {
        var wines = service
                .search(name, type, countries, volumes, priceRange)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(wines);
    }
}
