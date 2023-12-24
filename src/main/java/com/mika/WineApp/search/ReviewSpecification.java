package com.mika.WineApp.search;

import com.mika.WineApp.entities.Review;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface ReviewSpecification extends Specification<Review> {

    static ReviewSpecificationImpl of(ReviewSearchParams searchParams) {
        return new ReviewSpecificationImpl(searchParams);
    }

    @Override
    Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder builder);

}
