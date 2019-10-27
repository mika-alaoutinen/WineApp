package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Consider JpaRepository if more advanced functionality is needed!
//@RepositoryRestResource(collectionResourceRel = "reviews", path = "reviews")
@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findAll();
//    List<Review> findByMinRating(double minRating);
//    List<Review> findByMaxRating(double maxRating);
//    List<Review> findByDate(LocalDate date);
//    List<Review> findByAuthor(String name);
}
