package com.mika.WineApp.specifications;

import com.mika.WineApp.models.Wine;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class WineSpecification implements Specification<Wine> {
    private Integer[] priceRange;
    private Wine wine;

    public WineSpecification(Wine wine, Integer[] priceRange) {
        super();
        this.priceRange = priceRange;
        this.wine = wine;
    }

    public Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // Name and country:
        Expression<String> rootName = builder.lower(root.get("name"));
        Expression<String> rootCountry = builder.lower(root.get("country"));

        List<Predicate> predicates = new ArrayList<>(List.of(
                builder.like(rootName, formatString(wine.getName())),
                builder.like(rootCountry, formatString(wine.getCountry()))
        ));

        // Type:
        if (wine.getType() != null) {
            predicates.add(builder.equal(root.get("type"), wine.getType()));
        }

        // Price range:
        if (priceRange != null && priceRange.length == 2) {
            predicates.add(builder.between(root.get("price"), priceRange[0], priceRange[1]));
        }

        // Volume:
        if (wine.getVolume() != null) {
            predicates.add(builder.equal(root.get("volume"), wine.getVolume()));
        }

        // Return conjunction predicate:
        Predicate predicate = builder.conjunction();
        predicate.getExpressions().addAll(predicates);

        return predicate;
    }

    /**
     * Converts a string to lowercase and wraps it in query wildcards, like so: %string%.
     * @param attribute wineAttribute
     * @return formattedString
     */
    private String formatString(String attribute) {
        return attribute == null ? "%" : "%" + attribute.toLowerCase() + "%";
    }
}