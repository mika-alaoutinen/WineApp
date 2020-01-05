package com.mika.WineApp.specifications;

import com.mika.WineApp.models.Review;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewSpecification extends SuperSpecification implements Specification<Review> {
    private LocalDate[] dateRange;
    private Double[] ratingRange;
    private Review review;

    public ReviewSpecification(Review review, LocalDate[] dateRange, Double[] ratingRange) {
        super();
        this.dateRange = dateRange;
        this.ratingRange = ratingRange;
        this.review = review;
    }

    public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        // Author:
        Expression<String> rootAuthor = builder.lower(root.get("author"));
        predicates.add(builder.like(rootAuthor, super.formatString(review.getAuthor())));

        // Date range:
        if (dateRange != null && dateRange.length == 2) {
            predicates.add(builder.between(root.get("date"), dateRange[0], dateRange[1]));
        }

        // Rating range:
        if (ratingRange != null && ratingRange.length == 2) {
            predicates.add(builder.between(root.get("rating"), ratingRange[0], ratingRange[1]));
        }

        // Get results in descending order:
        query.orderBy(builder.desc(root.get("date")));

        return super.createConjunction(builder, predicates);
    }
}
