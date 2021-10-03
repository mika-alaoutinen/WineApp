package com.mika.WineApp.mappers;

import com.mika.WineApp.models.user.Role;
import com.mika.model.UserDTO;
import org.mapstruct.EnumMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @EnumMapping(
            configuration = "ROLE_",
            nameTransformationStrategy = MappingConstants.PREFIX_TRANSFORMATION
    )
    Role toModel(UserDTO.RolesEnum dto);

    @EnumMapping(
            configuration = "ROLE_",
            nameTransformationStrategy = MappingConstants.STRIP_PREFIX_TRANSFORMATION
    )
    UserDTO.RolesEnum toDTO(Role model);
}
