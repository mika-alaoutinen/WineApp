package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.OptionalDouble;

@RestController
public class WineController {
    private final WineService service;

    public WineController(WineRepository repository) {
        this.service = new WineService(repository);
    }

    @GetMapping("/wines")
    List<Wine> findAll() {
        return service.findAll();
    }

    @GetMapping("wines/name/{name}")
    List<Wine> findByName(@PathVariable String name) {
        return service.findByName(name);
    }

    @GetMapping("wines/type/{type}")
    List<Wine> findByType(@PathVariable WineType type) {
        return service.findByType(type);
    }

    @GetMapping("wines/country/{country}")
    List<Wine> findByCountry(@PathVariable String country) {
        return service.findByCountry(country);
    }

    @GetMapping("wines/quantity/{quantity}")
    List<Wine> findByQuantity(@PathVariable Double quantity) {
        return service.findByQuantity(quantity);
    }

    @GetMapping("/wines/price")
    List<Wine> findByPrice(@RequestParam(name = "minPrice") OptionalDouble minPrice,
                           @RequestParam(name = "maxPrice") OptionalDouble maxPrice) {

        return service.findByPrice(minPrice, maxPrice);
    }

    @GetMapping("wines/pairings/{foodPairings}")
    List<Wine> findByFoodPairings(@RequestParam List<String> foodPairings) {
        // Note: HTTP request has to be in the following format:
        // ?id=1,2,3 and NOT like this ?id=1&id=2&id=3
        return service.findByFoodPairings(foodPairings);
    }

    @GetMapping("wines/{id}")
    EntityModel<Wine> findById(@PathVariable Long id) {
        Wine wine = service.findById(id)
                .orElseThrow(() -> new WineNotFoundException(id));

        return new EntityModel<>(wine,
                linkTo(methodOn(WineController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(WineController.class).findAll()).withRel("wines"));
    }

    @PostMapping("/wines")
    Wine add(@RequestBody Wine newWine) {
        return service.add(newWine);
    }

    @PutMapping("wines/{id}")
    Wine edit(@RequestBody Wine editedWine, @PathVariable Long id) {
        return service.edit(editedWine, id);
    }

    @DeleteMapping("wines/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }
}