package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "reviews", path = "reviews")
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {

    Optional<Review> findById(Long id);
    List<Review> findAll();
    Review save(Review review);

    // Find by Review attributes:
    List<Review> findDistinctByAuthorIgnoreCase(String author);
    List<Review> findDistinctByDateBetweenOrderByDateDesc(LocalDate start, LocalDate end);
    List<Review> findDistinctByRatingBetweenOrderByRatingDesc(double minRating, double maxRating);
    Page<Review> findAllByOrderByDateDesc(Pageable limit);

    // Find by Wine attributes:
    List<Review> findByWineId(Long wineId);
    List<Review> findByWineNameContainingIgnoreCase(String wineName);
}
