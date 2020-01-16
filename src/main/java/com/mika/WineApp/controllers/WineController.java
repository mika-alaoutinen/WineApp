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
@RequestMapping("/wines")
public class WineController {
    private final WineService service;

    public WineController(WineRepository repository) {
        this.service = new WineServiceImpl(repository);
    }

// --- CRUD methods ---
    @GetMapping("/")
    public List<Wine> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Wine findById(@PathVariable Long id) {
        return service
                .findById(id)
                .orElseThrow(() -> new WineNotFoundException(id));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Wine add(@RequestBody Wine newWine) {
        return service.add(newWine);
    }

    @PutMapping("/{id}")
    public Wine edit(@PathVariable Long id, @RequestBody Wine editedWine) {
        return service.edit(id, editedWine);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

// --- Other methods ---
    @GetMapping("/count")
    public long count() {
        return service.count();
    }

    @GetMapping("/countries")
    public List<String> countries() {
        return service.findCountries();
    }

    @GetMapping("/descriptions")
    public List<String> descriptions() {
        return service.findDescriptions();
    }

    @GetMapping("/food-pairings")
    public List<String> foodPairings() {
        return service.findFoodPairings();
    }

    @GetMapping("/search")
    public List<Wine> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) List<String> countries,
            @RequestParam(required = false) List<Double> volumes,
            @RequestParam(required = false) Integer[] priceRange) {

        return service.search(name, type, countries, volumes, priceRange);
    }
}