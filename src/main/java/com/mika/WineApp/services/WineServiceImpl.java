package com.mika.WineApp.services;

import com.mika.WineApp.errors.InvalidWineTypeException;
import com.mika.WineApp.errors.WineNotFoundException;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineApp.specifications.WineSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class WineServiceImpl implements WineService {
    private final WineRepository repository;

// --- CRUD service methods ---
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

    public long count() {
        return repository.count();
    }

// --- Wine service methods ---
    public List<Wine> findByName(String name) {
        return repository.findDistinctByNameContainingIgnoreCase(name);
    }

    public List<Wine> findByType(String type) {
        return repository.findDistinctByType(parseWineType(type));
    }

    public List<Wine> findByCountry(String country) {
        return repository.findDistinctByCountryIgnoreCase(country);
    }

    public List<Wine> findByQuantity(double volume) {
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

    public List<Wine> search(String name,
                             String type,
                             String country,
                             Double minPrice,
                             Double maxPrice,
                             List<Double> volumes) {

        WineType wineType = null;
        if (type != null) {
            wineType = parseWineType(type);
        }

        List<Wine> wines = new ArrayList<>();

        if (volumes == null) {
            wines.add(new Wine(name, wineType, country, null, null, null, null, null));
        } else {
            WineType finalWineType = wineType;
            volumes.stream()
                    .map(volume -> new Wine(name, finalWineType, country, null, volume, null, null, null))
                    .forEach(wines::add);
        }

        return wines.stream()
                .map(wine -> new WineSpecification(wine, minPrice,  maxPrice))
                .map(repository::findAll)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

//        Wine filter = new Wine(name, wineType, country, null, null, null, null, null);
//
//        return volumes.stream()
//                .map(volume -> new WineSpecification(filter, minPrice, maxPrice))
//                .map(repository::findAll)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());

//        Wine wine = new Wine(name, wineType, country, null, null, null, null, null);


//        return repository.findAll(new WineSpecification(filter, minPrice, maxPrice, volumes));



//
//        Wine exampleWine = new Wine(name, wineType, country, price, volume, null, null, null);
//        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase();
//
//        var results = repository.findAll(Example.of(exampleWine, matcher));
//
//        return StreamSupport.stream(results.spliterator(), false)
//                .collect(Collectors.toList());
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
