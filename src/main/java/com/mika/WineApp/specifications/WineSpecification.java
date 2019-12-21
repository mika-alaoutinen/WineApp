package com.mika.WineApp.specifications;

import com.mika.WineApp.models.Wine;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
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

    private void test(Root<Wine> root, CriteriaBuilder builder, String rootAttribute, String wineAttribute, Predicate predicate) {
//        List<Predicate> predicates = List.of(builder.like(rootCountry, wineCountry), builder.like(rootName, wineName));
//        predicate.getExpressions().addAll(predicates);

        Expression<String> expression = builder.lower(root.get(rootAttribute));

        predicate.getExpressions()
                 .add(builder.like(expression, wineAttribute));
    }

    public Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();

        if (wine.getName() != null) {
            Expression<String> rootName = builder.lower(root.get("name"));
            String wineName = formatString(wine.getName());

            predicate.getExpressions()
                     .add(builder.like(rootName, wineName));
        }

        if (wine.getType() != null) {
            predicate.getExpressions()
                     .add(builder.equal(root.get("type"), wine.getType()));
        }

        if (wine.getCountry() != null) {
            Expression<String> rootCountry = builder.lower(root.get("country"));
            String wineCountry = formatString(wine.getCountry());

            predicate.getExpressions()
                     .add(builder.like(rootCountry, wineCountry));
        }

        // minPrice, maxPrice and volumes
        if (minPrice != null && maxPrice != null) {
            predicate.getExpressions()
                    .add(builder.between(root.get("price"), minPrice, maxPrice));
        }

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