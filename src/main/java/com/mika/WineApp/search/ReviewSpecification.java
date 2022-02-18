package com.mika.WineApp.search;

import com.mika.WineApp.entities.Review;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface ReviewSpecification extends Specification<Review> {

    static ReviewSpecificationImpl of(ReviewSearchParams searchParams) {
        return new ReviewSpecificationImpl(searchParams);
    }

    @Override
    Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder builder);

}
