package com.mika.WineApp.services;

import com.mika.WineApp.errors.notfound.NotFoundException;

import java.util.List;

public interface CrudService <EntityModel> {
    // Add operation is missing because it is different for wines and reviews.

    /**
     * Get all wines or reviews.
     * @return list of entities
     */
    List<EntityModel> findAll();

    /**
     * Find wine or review by ID.
     * @param id of entity
     * @return wine or review
     * @throws NotFoundException e
     */
    EntityModel findById(Long id) throws NotFoundException;

    /**
     * Edit wine or review.
     * @param id of persisted entity to edit
     * @param em edited wine or review
     * @return persisted entity model
     * @throws NotFoundException e
     */
    EntityModel edit(Long id, EntityModel em) throws NotFoundException;

    /**
     * Delete wine or review.
     * @param id of entity
     */
    void delete(Long id);

    /**
     * Get entity count in database.
     * @return Long count.
     */
    long count();
}