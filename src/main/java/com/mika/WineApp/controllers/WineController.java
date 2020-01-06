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

// --- CRUD methods ---
    @GetMapping(baseUrl)
    public List<Wine> findAll() {
        return service.findAll();
    }

    @GetMapping(baseUrl + "/{id}")
    public Wine findById(@PathVariable Long id) {
        return service
                .findById(id)
                .orElseThrow(() -> new WineNotFoundException(id));
    }

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

// --- Other methods ---
    @GetMapping(baseUrl + "/count")
    public long count() {
        return service.count();
    }

    @GetMapping(baseUrl + "/countries")
    public List<String> countries() {
        return service.getCountries();
    }

    @GetMapping(baseUrl + "/descriptions")
    public List<String> descriptions() {
        return service.getDescriptions();
    }

    @GetMapping(baseUrl + "/food-pairings")
    public List<String> foodPairings() {
        return service.getFoodPairings();
    }

    @GetMapping(baseUrl + "/search")
    public List<Wine> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) List<String> countries,
            @RequestParam(required = false) List<Double> volumes,
            @RequestParam(required = false) Integer[] priceRange) {

        return service.search(name, type, countries, volumes, priceRange);
    }
}