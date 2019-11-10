package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "wines", path = "wines")
public interface WineRepository extends PagingAndSortingRepository<Wine, Long> {

    Optional<Wine> findById(Long id);
    List<Wine> findAll();
    Wine save(Wine wine);

    List<Wine> findByNameContaining(String name);
    List<Wine> findByType(WineType type);
    List<Wine> findByCountry(String country);
    List<Wine> findByPriceBetween(double minPrice, double maxPrice);
    List<Wine> findByQuantity(double quantity);
    List<Wine> findByFoodPairingsIn(List<String> food);
}