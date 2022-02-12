package com.mika.WineApp.wines;

import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.entities.WineType;
import com.mika.WineApp.utils.Predicates;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class WineSpecification implements Specification<Wine> {
    private final List<Predicate> predicates = new ArrayList<>();
    private final String name;
    private final WineType type;
    private final List<String> countries;
    private final List<Double> volumes;
    private final List<Double> priceRange;

    @Override
    public Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        namePredicate(root, builder);
        typePredicate(root, builder);
        priceRangePredicate(root, builder);
        countryPredicate(root, builder);
        volumePredicate(root, builder);

        query.orderBy(createQueryOrder(root, builder));
        return Predicates.createConjunction(builder, predicates);
    }

    private void namePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (name != null && !name.isBlank()) {
            Expression<String> rootName = builder.lower(root.get("name"));
            Predicate predicate = builder.like(rootName, Predicates.formatString(name));
            predicates.add(predicate);
        }
    }

    private void typePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (type != null) {
            Predicate predicate = builder.equal(root.get("type"), type);
            predicates.add(predicate);
        }
    }

    private void countryPredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (countries != null && !countries.isEmpty()) {
            Expression<String> rootCountry = builder.lower(root.get("country"));

            var countryPredicates = countries
                    .stream()
                    .filter(country -> !country.isBlank())
                    .map(Predicates::formatString)
                    .map(country -> builder.like(rootCountry, country))
                    .collect(Collectors.toList());

            Predicate predicate = Predicates.createDisjunction(builder, countryPredicates);
            predicates.add(predicate);
        }
    }

    private void volumePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (volumes != null && !volumes.isEmpty()) {
            var volumePredicates = volumes
                    .stream()
                    .filter(Objects::nonNull)
                    .map(volume -> builder.equal(root.get("volume"), volume))
                    .collect(Collectors.toList());

            Predicate predicate = Predicates.createDisjunction(builder, volumePredicates);
            predicates.add(predicate);
        }
    }

    private void priceRangePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (priceRange != null && priceRange.size() == 2) {
            Predicate predicate = builder.between(root.get("price"), priceRange.get(0), priceRange.get(1));
            predicates.add(predicate);
        }
    }

    private List<Order> createQueryOrder(Root<Wine> root, CriteriaBuilder builder) {
        return List.of(
                builder.asc(root.get("type")),
                builder.asc(root.get("name"))
        );
    }
}