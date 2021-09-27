package com.mika.WineApp.mappers;

import com.mika.WineApp.models.wine.Wine;
import model.WineDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WineMapper {
    Wine toModel(WineDTO dto);

    WineDTO toDTO(Wine model);
}
