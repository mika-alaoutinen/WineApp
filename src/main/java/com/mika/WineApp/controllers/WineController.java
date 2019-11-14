package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.hateoas.WineModelAssembler;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WineController {
    private final WineService service;
    private final WineModelAssembler assembler;
    private static final String baseUrl = "wines/";

    public WineController(WineRepository repository, WineModelAssembler assembler) {
        this.service = new WineService(repository);
        this.assembler = assembler;
    }

    @GetMapping(baseUrl)
    public CollectionModel<EntityModel<Wine>> findAll() {
        var wines = service.findAll();
        return assembler.buildResponse(wines);
    }

    @GetMapping(baseUrl + "/name/{name}")
    public CollectionModel<EntityModel<Wine>> findByName(@PathVariable String name) {
        var wines = service.findByName(name);
        if (wines.isEmpty()) {
            throw new WineNotFoundException(name);
        }

        return assembler.buildResponse(wines);
    }

    @GetMapping(baseUrl + "/type/{type}")
    public CollectionModel<EntityModel<Wine>> findByType(@PathVariable String type) {
        var wines = service.findByType(type);
        return assembler.buildResponse(wines);
    }

    @GetMapping(baseUrl + "/country/{country}")
    public CollectionModel<EntityModel<Wine>> findByCountry(@PathVariable String country) {
        var wines = service.findByCountry(country);
        return assembler.buildResponse(wines);
    }

    @GetMapping(baseUrl + "/quantity/{quantity}")
    public CollectionModel<EntityModel<Wine>> findByQuantity(@PathVariable Double quantity) {
        var wines = service.findByQuantity(quantity);
        return assembler.buildResponse(wines);
    }

    @GetMapping(baseUrl + "/price")
    public CollectionModel<EntityModel<Wine>> findByPrice(
            @RequestParam(name = "minPrice", defaultValue = "0") double minPrice,
            @RequestParam(name = "maxPrice", defaultValue = "9999") double maxPrice) {

        var wines = service.findByPrice(minPrice, maxPrice);
        return assembler.buildResponse(wines);
    }

    @GetMapping(baseUrl + "/description")
    public CollectionModel<EntityModel<Wine>> findByDescription(
            @RequestParam(name = "desc") List<String> description) {

        var wines = service.findByDescription(description);
        return assembler.buildResponse(wines);
    }

    @GetMapping(baseUrl + "/pairing")
    public CollectionModel<EntityModel<Wine>> findByFoodPairings(
            @RequestParam(name = "pair") List<String> foodPairings) {

        var wines = service.findByFoodPairings(foodPairings);
        return assembler.buildResponse(wines);
    }

    @GetMapping(baseUrl + "/{id}")
    public EntityModel<Wine> findById(@PathVariable Long id) {
        Wine wine = service.findById(id)
                .orElseThrow(() -> new WineNotFoundException(id));

        return assembler.toModel(wine);
    }

// --- Add, edit and delete ---
    @PostMapping(baseUrl)
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Wine> add(@RequestBody Wine newWine) {
        Wine wine = service.add(newWine);
        return assembler.toModel(wine);
    }

    @PutMapping(baseUrl + "/{id}")
    public EntityModel<Wine> edit(@RequestBody Wine editedWine, @PathVariable Long id) {
        Wine wine = service.edit(editedWine, id);
        return assembler.toModel(wine);
    }

    @DeleteMapping(baseUrl + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}