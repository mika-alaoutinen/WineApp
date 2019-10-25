package com.mika.WineApp.controllers;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    Wine find(@PathVariable Long id) {
        Optional<Wine> opt = service.find(id);
        // TODO: Handle optionals!
        return opt.get();
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
