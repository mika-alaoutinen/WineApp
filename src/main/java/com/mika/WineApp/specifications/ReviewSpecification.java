package com.mika.WineApp.specifications;

import com.mika.WineApp.models.Review;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification extends SuperSpecification implements Specification<Review> {
    private Review review;

    public ReviewSpecification(Review review) {
        super();
        this.review = review;
    }

    public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        // Author:
        Expression<String> rootAuthor = builder.lower(root.get("author"));
        predicates.add(builder.like(rootAuthor, super.formatString(review.getAuthor())));

        // Date range:

        // Rating range:

        Predicate predicate = builder.conjunction();
        predicate.getExpressions().addAll(predicates);

        return super.toPredicate(builder, predicates);
    }
}
