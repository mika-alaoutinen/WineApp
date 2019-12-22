package com.mika.WineApp.specifications;

import com.mika.WineApp.models.Wine;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class WineSpecification implements Specification<Wine> {
    private Double minPrice;
    private Double maxPrice;
    private Wine wine;

    public WineSpecification(Wine wine, Double minPrice, Double maxPrice) {
        super();
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.wine = wine;
    }

    public Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // Name:
        Expression<String> rootName = builder.lower(root.get("name"));
        String wineName = formatString(wine.getName());

        // Country:
        Expression<String> rootCountry = builder.lower(root.get("country"));
        String wineCountry = formatString(wine.getCountry());

        List<Predicate> predicates = new ArrayList<>(List.of(
                builder.like(rootName, wineName),
                builder.like(rootCountry, wineCountry)));

        // Type:
        if (wine.getType() != null) {
            predicates.add(builder.equal(root.get("type"), wine.getType()));
        }

        // Price range:
        if (minPrice != null && maxPrice != null) {
            predicates.add(builder.between(root.get("price"), minPrice, maxPrice));
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
     * @param string wineAttribute
     * @return formattedString
     */
    private String formatString(String string) {
        return string == null ? "%" : "%" + string.toLowerCase() + "%";
    }
}