package com.mika.WineApp.search;

import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.utils.Predicates;
import com.mika.WineApp.wines.IWineSpecification;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WineSpecification implements IWineSpecification {
    private final List<Predicate> predicates = new ArrayList<>();
    private final WineSearchParams searchParams;

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

    @Override
    public IWineSpecification of(WineSearchParams searchParams) {
        return new WineSpecification(searchParams);
    }

    private Optional<Predicate> namePredicate(Root<Wine> root, CriteriaBuilder builder) {
        var rootName = builder.lower(root.get("name"));

        return searchParams
                .getName()
                .map(Predicates::formatString)
                .map(name -> builder.like(rootName, name));
    }

    private Optional<Predicate> typePredicate(Root<Wine> root, CriteriaBuilder builder) {
        return searchParams
                .getType()
                .map(type -> builder.equal(root.get("type"), type));
    }

    private Optional<Predicate> countryPredicate(Root<Wine> root, CriteriaBuilder builder) {
        var countries = searchParams.getCountries();

        if (countries.isEmpty()) {
            return Optional.empty();
        }

        var rootCountry = builder.lower(root.get("country"));

        var countryPredicates = countries
                .stream()
                .map(Predicates::formatString)
                .map(country -> builder.like(rootCountry, country))
                .toList();

        return Optional.of(Predicates.createDisjunction(builder, countryPredicates));
    }

    private Optional<Predicate> volumePredicate(Root<Wine> root, CriteriaBuilder builder) {
        var volumes = searchParams.getVolumes();

        if (volumes.isEmpty()) {
            return Optional.empty();
        }

        var volumePredicates = volumes
                .stream()
                .map(volume -> builder.equal(root.get("volume"), volume))
                .toList();

        return Optional.of(Predicates.createDisjunction(builder, volumePredicates));
    }

    private Optional<Predicate> priceRangePredicate(Root<Wine> root, CriteriaBuilder builder) {
        return searchParams
                .getPriceRange()
                .map(priceRange -> builder.between(root.get("price"), priceRange.getFirst(), priceRange.getSecond()));
    }

    private List<Order> createQueryOrder(Root<Wine> root, CriteriaBuilder builder) {
        return List.of(
                builder.asc(root.get("type")),
                builder.asc(root.get("name"))
        );
    }
}