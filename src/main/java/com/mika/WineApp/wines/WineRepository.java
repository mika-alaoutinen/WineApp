package com.mika.WineApp.wines;

import com.mika.WineApp.entities.Wine;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface WineRepository extends CrudRepository<Wine, Long>, JpaSpecificationExecutor<Wine> {
    boolean existsByName(String name);

    @Override
    Optional<Wine> findById(@Nonnull Long id);

    List<Wine> findAllByOrderByNameAsc();

    @Override
    Wine save(@Nonnull Wine wine);

    @Query(value = "SELECT DISTINCT country FROM wine ORDER BY country", nativeQuery = true)
    List<String> findAllCountries();

    @Query(value = "SELECT DISTINCT description FROM wine_descriptions ORDER BY description", nativeQuery = true)
    List<String> findAllDescriptions();

    @Query(value = "SELECT DISTINCT food_pairings FROM wine_food_pairings ORDER BY food_pairings", nativeQuery = true)
    List<String> findAllFoodPairings();
}