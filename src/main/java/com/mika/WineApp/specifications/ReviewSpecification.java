package com.mika.WineApp.specifications;

import com.mika.WineApp.models.review.Review;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public class ReviewSpecification extends SuperSpecification implements Specification<Review> {
    private final String author;
    private final LocalDate[] dateRange;
    private final Double[] ratingRange;

    public ReviewSpecification(String author, LocalDate[] dateRange, Double[] ratingRange) {
        super();
        this.author = author;
        this.dateRange = dateRange;
        this.ratingRange = ratingRange;
    }

    public Predicate toPredicate(@NotNull Root<Review> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder builder) {
        authorPredicate(root, builder);
        datePredicate(root, builder);
        ratingPredicate(root, builder);

        query.orderBy(createQueryOrder(root, builder));
        return super.createConjunction(builder, predicates);
    }

    private void authorPredicate(Root<Review> root, CriteriaBuilder builder) {
        if (author != null && !author.isBlank()) {
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

    private  List<Order> createQueryOrder(Root<Review> root, CriteriaBuilder builder) {
        return List.of(
                builder.desc(root.get("date")),
                builder.asc(root.get("author")),
                builder.desc(root.get("rating"))
        );
    }
}