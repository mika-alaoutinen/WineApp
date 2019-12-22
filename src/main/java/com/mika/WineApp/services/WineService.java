package com.mika.WineApp.services;

import com.mika.WineApp.models.Wine;

import java.util.List;

public interface WineService extends CrudService<Wine> {
    Wine add (Wine newWine);

    List<Wine> findByName(String name);
    List<Wine> findByType(String type);
    List<Wine> findByCountry(String country);
    List<Wine> findByQuantity(double quantity);
    List<Wine> findByPrice(double minPrice, double maxPrice);
    List<Wine> findByDescription(List<String> description);
    List<Wine> findByFoodPairings(List<String> foodPairings);

    List<Wine> search(String name, String type, String country, Double minPrice, Double maxPrice, List<Double> volumes);
}
