package com.mika.WineApp.controllers;

import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("wines/{id}")
    Wine findById(@PathVariable Long id) {
        return service.findById(id)
                      .orElseThrow(() -> new WineNotFoundException(id));
    }

    @GetMapping("wines/{name}")
    List<Wine> findByName(@PathVariable String name) {
        return service.findByName(name);
    }

    @GetMapping("wines/{type}")
    List<Wine> findByType(@PathVariable WineType type) {
        return service.findByType(type);
    }

    @GetMapping("wines/{country}")
    List<Wine> findByCountry(@PathVariable String country) {
        return service.findByCountry(country);
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