package com.mika.WineApp.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.List;

public abstract class SuperSpecification {

    /**
     * Converts a string to lowercase and wraps it in query wildcards, like so: %string%.
     * @param attribute wineAttribute
     * @return formattedString
     */
    protected String formatString(String attribute) {
        return attribute == null ? "%" : "%" + attribute.toLowerCase() + "%";
    }

    /**
     * Returns a conjunction of given predicates.
     * @param builder CriteriaBuilder.
     * @param predicates a list of predicates.
     * @return Predicate.
     */
    protected Predicate toPredicate(CriteriaBuilder builder, List<Predicate> predicates) {
        Predicate predicate = builder.conjunction();
        predicate.getExpressions().addAll(predicates);

        return predicate;
    }
}