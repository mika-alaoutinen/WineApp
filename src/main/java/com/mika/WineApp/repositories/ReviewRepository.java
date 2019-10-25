package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Author;
import com.mika.WineApp.models.Wine;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

// Consider JpaRepository if more advanced functionality is needed!
public interface ReviewRepository extends CrudRepository {
    List<Wine> findByMinRating(double minRating);
    List<Wine> findByMaxRating(double maxRating);
    List<Wine> findByDate(LocalDate date);
    List<Wine> findByAuthor(String name);
}
