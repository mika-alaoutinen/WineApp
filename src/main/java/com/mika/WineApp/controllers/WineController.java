package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import com.mika.WineApp.services.WineServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WineController {
    private final WineService service;
    private static final String baseUrl = "/wines";

    public WineController(WineRepository repository) {
        this.service = new WineServiceImpl(repository);
    }

// --- Find wines ---
    @GetMapping(baseUrl)
    public List<Wine> findAll() {
        return service.findAll();
    }

    @GetMapping(baseUrl + "/name/{name}")
    public List<Wine> findByName(@PathVariable String name) {
        return service.findByName(name);
    }

    @GetMapping(baseUrl + "/type/{type}")
    public List<Wine> findByType(@PathVariable String type) {
        return service.findByType(type);
    }

    @GetMapping(baseUrl + "/country/{country}")
    public List<Wine> findByCountry(@PathVariable String country) {
        return service.findByCountry(country);
    }

    @GetMapping(baseUrl + "/quantity/{quantity}")
    public List<Wine> findByQuantity(@PathVariable Double quantity) {
        return service.findByQuantity(quantity);
    }

    @GetMapping(baseUrl + "/price")
    public List<Wine> findByPrice(
            @RequestParam(name = "minPrice", defaultValue = "0") double minPrice,
            @RequestParam(name = "maxPrice", defaultValue = "9999") double maxPrice) {

        return service.findByPrice(minPrice, maxPrice);
    }

    @GetMapping(baseUrl + "/description")
    public List<Wine> findByDescription(
            @RequestParam(name = "desc") List<String> description) {

        return service.findByDescription(description);
    }

    @GetMapping(baseUrl + "/pairing")
    public List<Wine> findByFoodPairings(
            @RequestParam(name = "pair") List<String> foodPairings) {

        return service.findByFoodPairings(foodPairings);
    }

    @GetMapping(baseUrl + "/{id}")
    public Wine findById(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new WineNotFoundException(id));
    }

// --- Add, edit and delete ---
    @PostMapping(baseUrl)
    @ResponseStatus(HttpStatus.CREATED)
    public Wine add(@RequestBody Wine newWine) {
        return service.add(newWine);
    }

    @PutMapping(baseUrl + "/{id}")
    public Wine edit(@PathVariable Long id, @RequestBody Wine editedWine) {
        return service.edit(id, editedWine);
    }

    @DeleteMapping(baseUrl + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

// --- Additional functionality ---
    @GetMapping(baseUrl + "/count")
    public long count() {
        return service.count();
    }

    @GetMapping(baseUrl + "/search")
    public List<Wine> search(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "countries", required = false) List<String> countries,
            @RequestParam(name = "volumes", required = false) List<Double> volumes,
            @RequestParam(name = "priceRange", required = false) Integer[] priceRange) {

        return service.search(name, type, countries, volumes, priceRange);
    }
}