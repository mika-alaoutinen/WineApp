package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "wines", path = "wines")
public interface WineRepository extends PagingAndSortingRepository<Wine, Long> {
    List<Wine> findAll();
    Wine save(Wine wine);

    List<Wine> findByName(String name);
    List<Wine> findByType(WineType type);
    List<Wine> findByCountry(String country);
//    List<Wine> findByMinPrice(double minPrice); // Wines that are more expensive than minPrice
//    List<Wine> findByMaxPrice(double maxPrice); // Wines that are cheaper than maxPrice
//    List<Wine> findByQuantity(double quantity);
//    List<Wine> findByFoodPairings(List<String> food);
}