package com.mika.WineApp.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

public interface Predicates {

    static Predicate createConjunction(CriteriaBuilder builder, List<Predicate> predicates) {
        Predicate predicate = builder.conjunction();
        predicate
                .getExpressions()
                .addAll(predicates);
        return predicate;
    }

    static Predicate createDisjunction(CriteriaBuilder builder, List<Predicate> predicates) {
        Predicate predicate = builder.disjunction();
        predicate
                .getExpressions()
                .addAll(predicates);
        return predicate;
    }

    static String formatString(String attribute) {
        return "%" + attribute.toLowerCase() + "%";
    }
}