package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "reviews", path = "reviews")
public interface ReviewRepository extends CrudRepository<Review, Long> {

    Optional<Review> findById(Long id);
    List<Review> findAll();
    Review save(Review review);

    List<Review> findByWineId(Long wineId);
    List<Review> findByWineNameContainingIgnoreCase(String wineName);
//    List<Review> findByMinRating(double minRating);
//    List<Review> findByMaxRating(double maxRating);
//    List<Review> findByDate(LocalDate date);
//    List<Review> findByAuthor(String name);
}
