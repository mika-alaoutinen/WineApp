package com.mika.WineApp.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class SuperSpecification {
    protected List<Predicate> predicates;

    public SuperSpecification() {
        this.predicates = new ArrayList<>();
    }

    /**
     * Returns a conjunction of given predicates.
     *
     * @param builder    CriteriaBuilder.
     * @param predicates a list of predicates.
     * @return Predicate.
     */
    protected Predicate createConjunction(CriteriaBuilder builder, List<Predicate> predicates) {
        Predicate predicate = builder.conjunction();
        predicate
                .getExpressions()
                .addAll(predicates);
        return predicate;
    }

    /**
     * Returns a disjunction of given predicates.
     *
     * @param builder    CriteriaBuilder.
     * @param predicates a list of predicates.
     * @return Predicate.
     */
    protected Predicate createDisjunction(CriteriaBuilder builder, List<Predicate> predicates) {
        Predicate predicate = builder.disjunction();
        predicate
                .getExpressions()
                .addAll(predicates);
        return predicate;
    }

    /**
     * Converts a string to lowercase and wraps it in query wildcards, like so: %string%.
     *
     * @param attribute wineAttribute
     * @return formattedString
     */
    protected String formatString(String attribute) {
        return "%" + attribute.toLowerCase() + "%";
    }
}