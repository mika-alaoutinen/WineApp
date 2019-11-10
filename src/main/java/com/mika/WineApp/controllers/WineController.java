package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.hateoas.WineModelAssembler;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.OptionalDouble;

@RestController
public class WineController {
    private final WineService service;
    private final WineModelAssembler assembler;

    public WineController(WineRepository repository, WineModelAssembler assembler) {
        this.service = new WineService(repository);
        this.assembler = assembler;
    }

    @GetMapping("/wines")
    public CollectionModel<EntityModel<Wine>> findAll() {
        var wines = service.findAll();
        return assembler.buildResponse(wines);
    }

    @GetMapping("wines/name/{name}")
    public CollectionModel<EntityModel<Wine>> findByName(@PathVariable String name) {
        var wines = service.findByName(name);
        return assembler.buildResponse(wines);
    }

    @GetMapping("wines/type/{type}")
    public CollectionModel<EntityModel<Wine>> findByType(@PathVariable WineType type) {
        var wines = service.findByType(type);
        return assembler.buildResponse(wines);
    }

    @GetMapping("wines/country/{country}")
    public CollectionModel<EntityModel<Wine>> findByCountry(@PathVariable String country) {
        var wines = service.findByCountry(country);
        return assembler.buildResponse(wines);
    }

    @GetMapping("wines/quantity/{quantity}")
    public CollectionModel<EntityModel<Wine>> findByQuantity(@PathVariable Double quantity) {
        var wines = service.findByQuantity(quantity);
        return assembler.buildResponse(wines);
    }

    @GetMapping("/wines/price")
    public CollectionModel<EntityModel<Wine>> findByPrice(
            @RequestParam(name = "minPrice") OptionalDouble minPrice,
            @RequestParam(name = "maxPrice") OptionalDouble maxPrice) {

        var wines = service.findByPrice(minPrice, maxPrice);
        return assembler.buildResponse(wines);
    }

    @GetMapping("wines/pairings/{foodPairings}")
    public CollectionModel<EntityModel<Wine>> findByFoodPairings(
            @RequestParam List<String> foodPairings) {
        // Note: HTTP request has to be in the following format:
        // ?id=1,2,3 and NOT like this ?id=1&id=2&id=3

        var wines = service.findByFoodPairings(foodPairings);
        return assembler.buildResponse(wines);
    }

    @GetMapping("wines/{id}")
    public EntityModel<Wine> findById(@PathVariable Long id) {
        Wine wine = service.findById(id)
                .orElseThrow(() -> new WineNotFoundException(id));

        return assembler.toModel(wine);
    }

    @PostMapping("/wines")
    public ResponseEntity<?> add(@RequestBody Wine newWine) {
        Wine wine = service.add(newWine);
        var model = assembler.toModel(wine);

        return assembler.addLinks(model);
    }

    @PutMapping("wines/{id}")
    public EntityModel<Wine> edit(@RequestBody Wine editedWine, @PathVariable Long id) {
        Wine wine = service.edit(editedWine, id);
        return assembler.toModel(wine);
    }

    @DeleteMapping("wines/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}