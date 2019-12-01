package com.mika.WineApp.services;

import java.util.List;
import java.util.Optional;

public interface CrudService <EntityModel> {
    // Add operation is missing because it is different for wines and reviews.
    List<EntityModel> findAll();
    Optional<EntityModel> findById(Long id);
    EntityModel edit(Long id, EntityModel em);
    void delete(Long id);
    long count();
}