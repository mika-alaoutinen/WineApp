package com.mika.WineApp.services;

import com.mika.WineApp.errors.InvalidWineTypeException;
import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class WineService {
    private final WineRepository repository;

// --- Find wines ---
    public List<Wine> findAll() {
        return repository.findAll();
    }

    public List<Wine> findByName(String name) {
        return repository.findDistinctByNameContainingIgnoreCase(name);
    }

    public List<Wine> findByType(String type) {
        return repository.findDistinctByType(parseWineType(type));
    }

    public List<Wine> findByCountry(String country) {
        return repository.findDistinctByCountryIgnoreCase(country);
    }

    public List<Wine> findByVolume(double volume) {
        return repository.findDistinctByVolume(volume);
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

// --- Add, edit and delete ---
    public Wine add(Wine newWine) {
        return repository.save(newWine);
    }

    public Wine edit(Wine editedWine, Long id) {
        return repository.findById(id).map(wine -> {
            wine.setName(editedWine.getName());
            wine.setType(editedWine.getType());
            wine.setCountry(editedWine.getCountry());
            wine.setPrice(editedWine.getPrice());
            wine.setVolume(editedWine.getVolume());
            wine.setDescription(editedWine.getDescription());
            wine.setFoodPairings(editedWine.getFoodPairings());
            wine.setUrl(editedWine.getUrl());
            return repository.save(wine);
        }).orElseThrow(() -> new WineNotFoundException(id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

// --- Additional functionality ---
    public long count() {
        return repository.count();
    }

    public List<Wine> search(String name, String type, String country, Double price, Double volume) {
        WineType wineType = null;
        if (type != null) {
            wineType = parseWineType(type);
        }

        Wine exampleWine = new Wine(name, wineType, country, price, volume, null, null, null);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase();

        var results = repository.findAll(Example.of(exampleWine, matcher));

        return StreamSupport.stream(results.spliterator(), false)
                .collect(Collectors.toList());
    }

// --- Utility methods ---
    private WineType parseWineType(String type) {
        try {
            return WineType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Error in parsing wine type: " + e.getMessage());
            throw new InvalidWineTypeException(type);
        }
    }
}
