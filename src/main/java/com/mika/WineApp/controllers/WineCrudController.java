package com.mika.WineApp.controllers;

import com.mika.WineApp.mappers.WineMapper;
import com.mika.WineApp.services.WineService;
import com.mika.api.WinesCrudApi;
import com.mika.model.NewWineDTO;
import com.mika.model.WineDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WineCrudController implements WinesCrudApi {
    private final WineMapper mapper;
    private final WineService service;

    @Override
    public ResponseEntity<WineDTO> addWine(NewWineDTO newWine) {
        var savedWine = service.add(mapper.toModel(newWine));
        return new ResponseEntity<>(mapper.toDTO(savedWine), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteWine(Long id) {
        service.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<WineDTO> findWine(Long id) {
        var wine = service
                .findById(id)
                .map(mapper::toDTO);
        return ResponseEntity.of(wine);
    }

    @Override
    public ResponseEntity<List<WineDTO>> getWines() {
        var wines = service
                .findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(wines);
    }

    @Override
    public ResponseEntity<Boolean> isWineEditable(Long id) {
        return ResponseEntity.ok(service.isAllowedToEdit(id));
    }

    @Override
    public ResponseEntity<WineDTO> updateWine(Long id, NewWineDTO wineDTO) {
        var edited = service.edit(id, mapper.toModel(wineDTO));
        return ResponseEntity.ok(mapper.toDTO(edited));
    }
}