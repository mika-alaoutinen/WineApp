package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "reviews", path = "reviews")
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long>,
                                          JpaSpecificationExecutor<Review> {

    // Find reviews:
    Optional<Review> findById(Long id);
    List<Review> findAll();
    List<Review> findByWineId(Long wineId);
    List<Review> findByWineNameContainingIgnoreCase(String wineName);

    Review save(Review review);

    // Quick searches:
    Page<Review> findAllByOrderByDateDesc(Pageable limit);
    Page<Review> findAllByOrderByRatingDesc(Pageable limit);
    Page<Review> findAllByOrderByRatingAsc(Pageable limit);
}
