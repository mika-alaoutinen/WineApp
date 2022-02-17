package com.mika.WineApp.search;

import com.mika.WineApp.entities.Wine;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface WineSpecification extends Specification<Wine> {

    static WineSpecification of(WineSearchParams searchParams) {
        return new WineSpecificationImpl(searchParams);
    }

    @Override
    Predicate toPredicate(Root<Wine> root, CriteriaQuery<?> query, CriteriaBuilder builder);
}
