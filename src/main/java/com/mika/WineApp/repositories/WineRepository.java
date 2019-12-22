package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "wines", path = "wines")
public interface WineRepository extends PagingAndSortingRepository<Wine, Long>,
                                        JpaSpecificationExecutor<Wine> {

    Optional<Wine> findById(Long id);
    List<Wine> findAll();
    Wine save(Wine wine);

    List<Wine> findDistinctByNameContainingIgnoreCase(String name);
    List<Wine> findDistinctByType(WineType type);
    List<Wine> findDistinctByCountryIgnoreCase(String country);
    List<Wine> findDistinctByPriceBetween(double minPrice, double maxPrice);
    List<Wine> findDistinctByVolume(double volume);
    List<Wine> findDistinctByDescriptionInIgnoreCase(List<String> descriptions);
    List<Wine> findDistinctByFoodPairingsInIgnoreCase(List<String> food);
}