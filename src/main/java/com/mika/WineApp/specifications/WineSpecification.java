package com.mika.WineApp.specifications;

import com.mika.WineApp.models.Wine;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WineSpecification extends SuperSpecification implements Specification<Wine> {
    private Integer[] priceRange;
    private List<String> countries;
    private List<Double> volumes;
    private Wine wine;
    private List<Predicate> predicates;

    public WineSpecification(Wine wine, List<String> countries, List<Double> volumes, Integer[] priceRange) {
        super();
        this.countries = countries;
        this.volumes = volumes;
        this.priceRange = priceRange;
        this.wine = wine;
        this.predicates = new ArrayList<>();
    }

    public Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        namePredicate(root, builder);
        typePredicate(root, builder);
        priceRangePredicate(root, builder);
        countryPredicate(root, builder);
        volumePredicate(root, builder);

        return super.createConjunction(builder, predicates);
    }

    private void namePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (wine.getName() == null) {
            return;
        }

        Expression<String> rootName = builder.lower(root.get("name"));
        Predicate predicate = builder.like(rootName, super.formatString(wine.getName()));
        predicates.add(predicate);
    }

    private void typePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (wine.getType() == null) {
            return;
        }

        Predicate predicate = builder.equal(root.get("type"), wine.getType());
        predicates.add(predicate);
    }

    private void priceRangePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (priceRange == null || priceRange.length != 2) {
            return;
        }

        Predicate predicate = builder.between(root.get("price"), priceRange[0], priceRange[1]);
        predicates.add(predicate);
    }

    private void countryPredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (countries == null || countries.isEmpty()) {
            return;
        }

        Expression<String> rootCountry = builder.lower(root.get("country"));

        var countryPredicates = countries.stream()
                .map(super::formatString)
                .map(country -> builder.like(rootCountry, country))
                .collect(Collectors.toList());

        Predicate predicate = super.createDisjunction(builder, countryPredicates);
        predicates.add(predicate);
    }

    private void volumePredicate(Root<Wine> root, CriteriaBuilder builder) {
        if (volumes == null || volumes.isEmpty()) {
            return;
        }

        var volumePredicates = volumes.stream()
                .map(volume -> builder.equal(root.get("volume"), volume))
                .collect(Collectors.toList());

        Predicate predicate = super.createDisjunction(builder, volumePredicates);
        predicates.add(predicate);
    }
}