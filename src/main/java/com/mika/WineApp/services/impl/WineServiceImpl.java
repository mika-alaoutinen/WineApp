package com.mika.WineApp.services.impl;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.errors.notfound.NotFoundException;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.models.wine.WineType;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.services.WineService;
import com.mika.WineApp.specifications.WineSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WineServiceImpl implements WineService {
    private final WineRepository repository;

// --- CRUD methods ---
    public List<Wine> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    public Wine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(new Wine(), id));
    }

    public Wine add(Wine newWine) {
        String name = newWine.getName();

        if (!isValid(name)) {
            throw new BadRequestException(newWine, name);
        }

        return repository.save(newWine);
    }

    public Wine edit(Long id, Wine editedWine) {
        Wine wine = findById(id);

        wine.setName(editedWine.getName());
        wine.setType(editedWine.getType());
        wine.setCountry(editedWine.getCountry());
        wine.setPrice(editedWine.getPrice());
        wine.setVolume(editedWine.getVolume());
        wine.setDescription(editedWine.getDescription());
        wine.setFoodPairings(editedWine.getFoodPairings());
        wine.setUrl(editedWine.getUrl());

        return repository.save(wine);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

// --- Other methods ---
    public long count() {
        return repository.count();
    }

    public boolean isValid(String name) {
        return !repository.existsByName(name);
    }

    public List<String> findCountries() {
        return repository.findAllCountries();
    }

    public List<String> findDescriptions() {
        return repository.findAllDescriptions();
    }

    public List<String> findFoodPairings() {
        return repository.findAllFoodPairings();
    }

    public List<Wine> search(String name,
                             String type,
                             List<String> countries,
                             List<Double> volumes,
                             Integer[] priceRange) {

        if (name == null && type == null && countries == null && volumes == null && priceRange == null) {
            return new ArrayList<>();
        }

        WineType wineType = null;
        if (type != null) {
            wineType = parseWineType(type);
        }

        return repository.findAll(new WineSpecification(name, wineType, countries, volumes, priceRange));
    }

// Utility methods:
    private WineType parseWineType(String type) {
        try {
            return WineType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(WineType.OTHER, type);
        }
    }
}