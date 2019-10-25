package com.mika.WineApp.services;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.repositories.WineRepository;

import java.util.List;
import java.util.Optional;

public class WineService {
    private final WineRepository repository;

    public WineService(WineRepository repository) {
        this.repository = repository;
    }

    public List<Wine> findAll() {
        return repository.findAll();
    }

    public Optional<Wine> find(long id) {
        return repository.findById(id);
    }

    public Wine add(Wine newWine) {
        return repository.save(newWine);
    }

    public Wine edit(Wine wine, Long id) {
        // TODO: Optionals
        Optional<Wine> oldWine = repository.findById(id);
        return wine;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
