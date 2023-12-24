package com.mika.WineApp.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import java.util.Collection;
import java.util.List;

public interface Predicates {

    static Predicate createConjunction(CriteriaBuilder builder, List<Predicate> predicates) {
        return builder.and(toArray(predicates));
    }

    static Predicate createDisjunction(CriteriaBuilder builder, List<Predicate> predicates) {
        return builder.or(toArray(predicates));
    }

    static String formatString(String attribute) {
        return "%" + attribute.toLowerCase() + "%";
    }

    // https://stackoverflow.com/questions/74946324/spring-boot-3-jpa-specification-not-filtering-with-jakarta
    private static Predicate[] toArray(Collection<Predicate> predicates) {
        return predicates.toArray(new Predicate[0]);
    }
}