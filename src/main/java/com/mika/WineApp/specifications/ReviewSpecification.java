package com.mika.WineApp.specifications;

import com.mika.WineApp.models.Review;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewSpecification extends SuperSpecification implements Specification<Review> {
    private String author;
    private LocalDate[] dateRange;
    private Double[] ratingRange;

    public ReviewSpecification(String author, LocalDate[] dateRange, Double[] ratingRange) {
        super();
        this.author = author;
        this.dateRange = dateRange;
        this.ratingRange = ratingRange;
    }

    public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        authorPredicate(root, builder);
        datePredicate(root, builder);
        ratingPredicate(root, builder);

        // Get results in descending order:
        query.orderBy(builder.desc(root.get("date")));

        return super.createConjunction(builder, predicates);
    }

    private void authorPredicate(Root<Review> root, CriteriaBuilder builder) {
        if (author != null && !author.isEmpty()) {
            Expression<String> rootAuthor = builder.lower(root.get("author"));
            Predicate predicate = builder.like(rootAuthor, super.formatString(author));
            super.predicates.add(predicate);
        }
    }

    private void datePredicate(Root<Review> root, CriteriaBuilder builder) {
        if (dateRange != null && dateRange.length == 2) {
            Predicate predicate = builder.between(root.get("date"), dateRange[0], dateRange[1]);
            super.predicates.add(predicate);
        }
    }

    private void ratingPredicate(Root<Review> root, CriteriaBuilder builder) {
        if (ratingRange != null && ratingRange.length == 2) {
            Predicate predicate = builder.between(root.get("rating"), ratingRange[0], ratingRange[1]);
            super.predicates.add(predicate);
        }
    }
}