package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.hateoas.WineModelAssembler;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

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
        var wineModels = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(wineModels,
                linkTo(methodOn(WineController.class).findAll()).withSelfRel());
    }

    @GetMapping("wines/name/{name}")
    public List<Wine> findByName(@PathVariable String name) {
        return service.findByName(name);
    }

    @GetMapping("wines/type/{type}")
    public List<Wine> findByType(@PathVariable WineType type) {
        return service.findByType(type);
    }

    @GetMapping("wines/country/{country}")
    public List<Wine> findByCountry(@PathVariable String country) {
        return service.findByCountry(country);
    }

    @GetMapping("wines/quantity/{quantity}")
    public List<Wine> findByQuantity(@PathVariable Double quantity) {
        return service.findByQuantity(quantity);
    }

    @GetMapping("/wines/price")
    public List<Wine> findByPrice(@RequestParam(name = "minPrice") OptionalDouble minPrice,
                           @RequestParam(name = "maxPrice") OptionalDouble maxPrice) {

        return service.findByPrice(minPrice, maxPrice);
    }

    @GetMapping("wines/pairings/{foodPairings}")
    public List<Wine> findByFoodPairings(@RequestParam List<String> foodPairings) {
        // Note: HTTP request has to be in the following format:
        // ?id=1,2,3 and NOT like this ?id=1&id=2&id=3
        return service.findByFoodPairings(foodPairings);
    }

    @GetMapping("wines/{id}")
    public EntityModel<Wine> findById(@PathVariable Long id) {
        Wine wine = service.findById(id)
                .orElseThrow(() -> new WineNotFoundException(id));

        return assembler.toModel(wine);
    }

    @PostMapping("/wines")
    public Wine add(@RequestBody Wine newWine) {
        return service.add(newWine);
    }

    @PutMapping("wines/{id}")
    public Wine edit(@RequestBody Wine editedWine, @PathVariable Long id) {
        return service.edit(editedWine, id);
    }

    @DeleteMapping("wines/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}