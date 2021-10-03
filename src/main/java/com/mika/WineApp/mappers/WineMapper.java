package com.mika.WineApp.mappers;

import com.mika.WineApp.models.wine.Wine;
import com.mika.model.WineDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WineMapper {

    @Mapping(ignore = true, target = "reviews")
    @Mapping(ignore = true, target = "user")
    Wine toModel(WineDTO dto);

    WineDTO toDTO(Wine model);
}
