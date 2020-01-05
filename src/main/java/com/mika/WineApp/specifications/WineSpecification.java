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

    public WineSpecification(Wine wine, List<String> countries, List<Double> volumes, Integer[] priceRange) {
        super();
        this.countries = countries;
        this.volumes = volumes;
        this.priceRange = priceRange;
        this.wine = wine;
    }

    public Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        // Name:
        if (wine.getName() != null) {
            Expression<String> rootName = builder.lower(root.get("name"));
            predicates.add(builder.like(rootName, super.formatString(wine.getName())));
        }

        // Type:
        if (wine.getType() != null) {
            predicates.add(builder.equal(root.get("type"), wine.getType()));
        }

        // Country:
        if (countries != null && !countries.isEmpty()) {
            Expression<String> rootCountry = builder.lower(root.get("country"));

            var countryPredicates = countries.stream()
                    .map(super::formatString)
                    .map(country -> builder.like(rootCountry, country))
                    .collect(Collectors.toList());

            Predicate disjunction = super.createDisjunction(builder, countryPredicates);
            predicates.add(disjunction);
        }

        // Price range:
        if (priceRange != null && priceRange.length == 2) {
            predicates.add(builder.between(root.get("price"), priceRange[0], priceRange[1]));
        }

        // Volume:
        if (volumes != null && !volumes.isEmpty()) {
            var volumePredicates = volumes.stream()
                    .map(volume -> builder.equal(root.get("volume"), volume))
                    .collect(Collectors.toList());

            Predicate disjunction = super.createDisjunction(builder, volumePredicates);
            predicates.add(disjunction);
        }

        return super.createConjunction(builder, predicates);
    }
}