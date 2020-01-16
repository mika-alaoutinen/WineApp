package com.mika.WineApp.services;

import com.mika.WineApp.models.Wine;

import java.util.List;

public interface WineService extends CrudService<Wine> {
    List<String> findCountries();
    List<String> findDescriptions();
    List<String> findFoodPairings();

    Wine add (Wine newWine);

    List<Wine> search(String name,
                      String type,
                      List<String> countries,
                      List<Double> volumes,
                      Integer[] priceRange);
}
