package com.mika.WineApp.search;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.utils.Predicates;
import lombok.AllArgsConstructor;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public class ReviewSpecificationImpl implements ReviewSpecification {
    private final ReviewSearchParams searchParams;

    @Override
    public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        var author = authorPredicate(root, builder);
        var dates = datePredicate(root, builder);
        var ratings = ratingPredicate(root, builder);

        var predicates = Stream
                .of(author, dates, ratings)
                .flatMap(Optional::stream)
                .toList();

        query.orderBy(createQueryOrder(root, builder));
        return Predicates.createConjunction(builder, predicates);
    }

    private Optional<Predicate> authorPredicate(Root<Review> root, CriteriaBuilder builder) {
        var rootAuthor = builder.lower(root.get("author"));
        return searchParams
                .getAuthor()
                .map(Predicates::formatString)
                .map(author -> builder.like(rootAuthor, author));
    }

    private Optional<Predicate> datePredicate(Root<Review> root, CriteriaBuilder builder) {
        return searchParams
                .getDateRange()
                .map(dates -> builder.between(root.get("date"), dates.getFirst(), dates.getSecond()));
    }

    private Optional<Predicate> ratingPredicate(Root<Review> root, CriteriaBuilder builder) {
        return searchParams
                .getRatingRange()
                .map(ratings -> builder.between(root.get("rating"), ratings.getFirst(), ratings.getSecond()));
    }

    private List<Order> createQueryOrder(Root<Review> root, CriteriaBuilder builder) {
        return List.of(
                builder.desc(root.get("date")),
                builder.asc(root.get("author")),
                builder.desc(root.get("rating"))
        );
    }
}