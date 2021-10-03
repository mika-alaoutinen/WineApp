package com.mika.WineApp.mappers;

import com.mika.WineApp.models.user.Role;
import com.mika.model.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @ValueMapping(source = "GUEST", target = "ROLE_GUEST")
    @ValueMapping(source = "USER", target = "ROLE_USER")
    @ValueMapping(source = "ADMIN", target = "ROLE_ADMIN")
    Role toModel(UserDTO.RolesEnum dto);

    @ValueMapping(source = "ROLE_GUEST", target = "GUEST")
    @ValueMapping(source = "ROLE_USER", target = "USER")
    @ValueMapping(source = "ROLE_ADMIN", target = "ADMIN")
    UserDTO.RolesEnum toDTO(Role model);
}
