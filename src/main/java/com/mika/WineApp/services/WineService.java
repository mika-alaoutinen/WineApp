package com.mika.WineApp.services;

import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class WineService {
    private final WineRepository repository;

    public WineService(WineRepository repository) {
        this.repository = repository;
    }

// Find wines:
    public List<Wine> findAll() {
        return repository.findAll();
    }

    public List<Wine> findByName(String name) {
        return repository.findByNameContaining(name);
    }

    public List<Wine> findByType(WineType type) {
        return repository.findByType(type);
    }

    public List<Wine> findByCountry(String country) {
        return repository.findByCountry(country);
    }

    public List<Wine> findByQuantity(double quantity) {
        return repository.findByQuantity(quantity);
    }

    public List<Wine> findByPrice(OptionalDouble minPrice, OptionalDouble maxPrice) {
        double min = minPrice.orElse(0);
        double max = maxPrice.orElse(9999);
        return repository.findByPriceBetween(min, max);
    }

    public List<Wine> findByFoodPairings(List<String> foodPairings) {
        return repository.findByFoodPairingsIn(foodPairings);
    }

    public Optional<Wine> findById(Long id) {
        return repository.findById(id);
    }

// Add, edit and delete:
    public Wine add(Wine newWine) {
        return repository.save(newWine);
    }

    public Wine edit(Wine editedWine, Long id) {
        return repository.findById(id).map(wine -> {
            wine.setName(editedWine.getName());
            wine.setType(editedWine.getType());
            wine.setCountry(editedWine.getCountry());
            wine.setPrice(editedWine.getPrice());
            wine.setQuantity(editedWine.getQuantity());
            wine.setDescription(editedWine.getDescription());
            wine.setFoodPairings(editedWine.getFoodPairings());
            wine.setUrl(editedWine.getUrl());
            return repository.save(wine);
        }).orElseThrow(() -> new WineNotFoundException(id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
