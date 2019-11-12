package com.mika.WineApp.services;

import com.mika.WineApp.errors.InvalidWineTypeException;
import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return repository.findDistinctByNameContainingIgnoreCase(name);
    }

    public List<Wine> findByType(String type) {
        try {
            WineType wineType = WineType.valueOf(type.toUpperCase());
            return repository.findDistinctByType(wineType);
        } catch (IllegalArgumentException e) {
            throw new InvalidWineTypeException(type);
        }
    }

    public List<Wine> findByCountry(String country) {
        return repository.findDistinctByCountryIgnoreCase(country);
    }

    public List<Wine> findByQuantity(double quantity) {
        return repository.findDistinctByQuantity(quantity);
    }

    public List<Wine> findByPrice(double minPrice, double maxPrice) {
        return repository.findDistinctByPriceBetween(minPrice, maxPrice);
    }

    public List<Wine> findByDescription(List<String> description) {
        var wines = repository.findDistinctByDescriptionInIgnoreCase(description);

        return wines.stream()
                .filter(wine -> wine.getDescription().containsAll(description))
                .collect(Collectors.toList());
    }

    public List<Wine> findByFoodPairings(List<String> foodPairings) {
        var wines = repository.findDistinctByFoodPairingsInIgnoreCase(foodPairings);

        return wines.stream()
                .filter(wine -> wine.getFoodPairings().containsAll(foodPairings))
                .collect(Collectors.toList());
    }

    public Optional<Wine> findById(Long id) {
        return repository.findById(id);
    }

// Add, edit and delete:
    public Optional<Wine> find(Long id) {
        return repository.findById(id);
    }

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
