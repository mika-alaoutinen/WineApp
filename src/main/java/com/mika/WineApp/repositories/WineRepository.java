package com.mika.WineApp.repositories;

import com.mika.WineApp.models.Wine;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "wines", path = "wines")
public interface WineRepository extends PagingAndSortingRepository<Wine, Long>,
                                        JpaSpecificationExecutor<Wine> {

    Optional<Wine> findById(Long id);
    List<Wine> findAll();
    Wine save(Wine wine);
}