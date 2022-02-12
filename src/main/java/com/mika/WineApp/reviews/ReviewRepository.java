package com.mika.WineApp.reviews;

import com.mika.WineApp.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    // Find reviews:
    @Override
    Optional<Review> findById(Long id);

    List<Review> findAllByOrderByDateDesc();

    List<Review> findByWineId(Long wineId);

    List<Review> findByWineNameContainingIgnoreCase(String wineName);

    @Override
    Review save(Review review);

    // Quick searches:
    Page<Review> findAllDistinctByOrderByDateDesc(Pageable limit);

    Page<Review> findAllByOrderByRatingDesc(Pageable limit);

    Page<Review> findAllByOrderByRatingAsc(Pageable limit);
}