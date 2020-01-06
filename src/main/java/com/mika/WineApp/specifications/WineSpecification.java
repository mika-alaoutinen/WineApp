package com.mika.WineApp.specifications;

import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WineSpecification extends SuperSpecification implements Specification<Wine> {
    private String name;
    private WineType type;
    private Integer[] priceRange;
    private List<String> countries;
    private List<Double> volumes;

    public WineSpecification(String name, WineType type, List<String> countries, List<Double> volumes, Integer[] priceRange) {
        super();
        this.name = name;
        this.type = type;
        this.countries = countries;
        this.volumes = volumes;
        this.priceRange = priceRange;
    }

    public Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        namePredicate(root, builder);
        typePredicate(root, builder);
        priceRangePredicate(root, builder);
        countryPredicate(root, builder);
        volumePredicate(root, builder);

        return super.createConjunction(builder, super.predicates);
    }

    private void namePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (name != null && !name.isBlank()) {
            Expression<String> rootName = builder.lower(root.get("name"));
            Predicate predicate = builder.like(rootName, super.formatString(name));
            super.predicates.add(predicate);
        }
    }

    private void typePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (type != null) {
            Predicate predicate = builder.equal(root.get("type"), type);
            super.predicates.add(predicate);
        }
    }

    private void priceRangePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (priceRange != null && priceRange.length == 2) {
            Predicate predicate = builder.between(root.get("price"), priceRange[0], priceRange[1]);
            super.predicates.add(predicate);
        }
    }

    private void countryPredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (countries != null && !countries.isEmpty()) {
            Expression<String> rootCountry = builder.lower(root.get("country"));

            var countryPredicates = countries.stream()
                    .filter(country -> !country.isBlank())
                    .map(super::formatString)
                    .map(country -> builder.like(rootCountry, country))
                    .collect(Collectors.toList());

            Predicate predicate = super.createDisjunction(builder, countryPredicates);
            super.predicates.add(predicate);
        }
    }

    private void volumePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (volumes != null && !volumes.isEmpty()) {
            var volumePredicates = volumes.stream()
                    .filter(Objects::nonNull)
                    .map(volume -> builder.equal(root.get("volume"), volume))
                    .collect(Collectors.toList());

            Predicate predicate = super.createDisjunction(builder, volumePredicates);
            super.predicates.add(predicate);
        }
    }
}