package com.mika.WineApp.mappers;

import com.mika.WineApp.entities.Wine;
import com.mika.model.NewWineDTO;
import com.mika.model.WineDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WineMapper {

    WineDTO toDTO(Wine model);

    Wine toModel(NewWineDTO newDto);
}
