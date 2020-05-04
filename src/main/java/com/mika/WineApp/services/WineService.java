package com.mika.WineApp.services;

import com.mika.WineApp.errors.badrequest.BadRequestException;
import com.mika.WineApp.models.wine.Wine;

import java.util.List;

public interface WineService extends CrudService<Wine> {

    /**
     * Save new wine to repository. If wine with the same new already exists in repository,
     * new wine will not be saved.
     * @param newWine to be added
     * @return saved wine
     * @throws BadRequestException e
     */
    Wine add (Wine newWine) throws BadRequestException;

    /**
     * Find all distinct countries from wine entries.
     * @return list of countries
     */
    List<String> findCountries();

    /**
     * Find all distinct wine description keywords.
     * @return list of keywords
     */
    List<String> findDescriptions();

    /**
     * Find all distinct food pairings keywords/phrases for wines.
     * @return list of food pairings
     */
    List<String> findFoodPairings();

    /**
     * Check if wine with a given name exists in the repository.
     * Same wine should not be saved twice.
     * @param name of wine
     * @return boolean
     */
    boolean isValid(String name);

    /**
     * Find wines that match the given criteria. One or many criteria may be given.
     * @param name of wine
     * @param type of wine
     * @param countries to find
     * @param volumes to find
     * @param priceRange to find
     * @return list of wines matching given criteria
     * @throws BadRequestException e
     */
    List<Wine> search(
            String name, String type, List<String> countries, List<Double> volumes, Integer[] priceRange)
            throws BadRequestException;
}
