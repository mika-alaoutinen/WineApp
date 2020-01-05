package com.mika.WineApp.services;

import com.mika.WineApp.errors.InvalidWineTypeException;
import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.specifications.WineSpecification;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WineServiceImpl implements WineService {
    private final WineRepository repository;

// --- CRUD methods ---
    public List<Wine> findAll() {
        return repository.findAll();
    }

    public Optional<Wine> findById(Long id) {
        return repository.findById(id);
    }

    public Wine add(Wine newWine) {
        return repository.save(newWine);
    }

    public Wine edit(Long id, Wine editedWine) {
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

// --- Other methods ---
    public long count() {
        return repository.count();
    }

    public List<Wine> search(String name,
                             String type,
                             List<String> countries,
                             List<Double> volumes,
                             Integer[] priceRange) {

        WineType wineType = null;
        if (type != null) {
            wineType = parseWineType(type);
        }

        Wine wine = new Wine(name, wineType, null, null, null, null, null, null);
        return repository.findAll(new WineSpecification(wine, countries, volumes, priceRange));
    }

// Utility methods:
    private WineType parseWineType(String type) {
        try {
            return WineType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Error in parsing wine type: " + e.getMessage());
            throw new InvalidWineTypeException(type);
        }
    }
}