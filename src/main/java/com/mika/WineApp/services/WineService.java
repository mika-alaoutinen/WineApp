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

    public Optional<Wine> find(Long id) {
        return repository.findById(id);
    }

    public Wine add(Wine newWine) {
        return repository.save(newWine);
    }

    public Wine edit(Wine editedWine, Long id) {
        repository.findById(id).ifPresent(wine -> {
                wine.setName(editedWine.getName());
                wine.setType(editedWine.getType());
                wine.setCountry(editedWine.getCountry());
                wine.setPrice(editedWine.getPrice());
                wine.setQuantity(editedWine.getQuantity());
                wine.setDescription(editedWine.getDescription());
                wine.setFoodPairings(editedWine.getFoodPairings());
                wine.setUrl(editedWine.getUrl());
        });

        return editedWine;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
