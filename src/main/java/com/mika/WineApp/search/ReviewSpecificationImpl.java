package com.mika.WineApp.search;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.utils.Predicates;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ReviewSpecificationImpl implements Specification<Review> {
    private final ReviewSearchParams searchParams;

    private final List<Predicate> predicates = new ArrayList<>();
    private final String author = "";
    private final List<LocalDate> dateRange = List.of();
    private final List<Double> ratingRange = List.of();

    @Override
    public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        authorPredicate(root, builder);
        datePredicate(root, builder);
        ratingPredicate(root, builder);

        query.orderBy(createQueryOrder(root, builder));
        return Predicates.createConjunction(builder, predicates);
    }

    private void authorPredicate(Root<Review> root, CriteriaBuilder builder) {
        var rootAuthor = builder.lower(root.get("author"));
        Predicate predicate = builder.like(rootAuthor, Predicates.formatString(author));
        predicates.add(predicate);
    }

    private void datePredicate(Root<Review> root, CriteriaBuilder builder) {
        if (dateRange != null && dateRange.size() == 2) {
            Predicate predicate = builder.between(root.get("date"), dateRange.get(0), dateRange.get(1));
            predicates.add(predicate);
        }
    }

    private void ratingPredicate(Root<Review> root, CriteriaBuilder builder) {
        if (ratingRange != null && ratingRange.size() == 2) {
            Predicate predicate = builder.between(root.get("rating"), ratingRange.get(0), ratingRange.get(1));
            predicates.add(predicate);
        }
    }

    private List<Order> createQueryOrder(Root<Review> root, CriteriaBuilder builder) {
        return List.of(
                builder.desc(root.get("date")),
                builder.asc(root.get("author")),
                builder.desc(root.get("rating"))
        );
    }
}