package com.mika.WineApp.mappers;

import com.mika.WineApp.models.Role;
import com.mika.model.RoleDTO;
import org.mapstruct.EnumMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @EnumMapping(
            configuration = "ROLE_",
            nameTransformationStrategy = MappingConstants.PREFIX_TRANSFORMATION
    )
    Role toModel(RoleDTO dto);

    @EnumMapping(
            configuration = "ROLE_",
            nameTransformationStrategy = MappingConstants.STRIP_PREFIX_TRANSFORMATION
    )
    RoleDTO toDTO(Role model);
}
