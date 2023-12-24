package com.mika.WineApp.search;

import com.mika.WineApp.entities.Wine;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface WineSpecification extends Specification<Wine> {

    static WineSpecification of(WineSearchParams searchParams) {
        return new WineSpecificationImpl(searchParams);
    }

    @Override
    Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder);
}
