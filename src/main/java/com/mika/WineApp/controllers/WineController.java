package com.mika.WineApp.controllers;

import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.services.WineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/wines", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Wines API", description = "Contains CRUD operations, search functionality and ability to retrieve keyword lists.")
public class WineController {
    private final WineService service;

// --- CRUD methods ---
    @Operation(summary = "Get all wines", description = "Returns all wines ordered alphabetically by name.")
    @GetMapping()
    public List<Wine> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Get one wine")
    @GetMapping("{id}")
    public Wine findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Add new wine")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Wine add(@RequestBody Wine newWine) {
        return service.add(newWine);
    }

    @Operation(summary = "Edit wine")
    @PutMapping("{id}")
    public Wine edit(@PathVariable Long id, @RequestBody Wine editedWine) {
        return service.edit(id, editedWine);
    }

    @Operation(summary = "Delete wine")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

// --- Other methods ---
    @Operation(summary = "Get wine count", description = "Returns total count of wines in the application as long.")
    @GetMapping("count")
    public long count() {
        return service.count();
    }

    @Operation(summary = "Get list of distinct countries", description = "Returns list of all distinct countries found as wine attributes.")
    @GetMapping("countries")
    public List<String> countries() {
        return service.findCountries();
    }

    @Operation(summary = "Get list of descriptions", description = "Returns list of all distinct wine descriptions found as wine attributes.")
    @GetMapping("descriptions")
    public List<String> descriptions() {
        return service.findDescriptions();
    }

    @Operation(summary = "Get list of food pairings", description = "Returns list of all distinct wine-food pairings found as wine attributes.")
    @GetMapping("food-pairings")
    public List<String> foodPairings() {
        return service.findFoodPairings();
    }

    @Operation(summary = "Search wines", description = "Search for wines based on their name, type, countries, volumes and prices.")
    @GetMapping("search")
    public List<Wine> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) List<String> countries,
            @RequestParam(required = false) List<Double> volumes,
            @RequestParam(required = false) Integer[] priceRange) {

        return service.search(name, type, countries, volumes, priceRange);
    }

    @Operation(summary = "Validate wine's name", description = "Check that wine's name is unique.")
    @GetMapping("validate")
    public boolean validate(@RequestParam String name) {
        return service.isValid(name);
    }
}