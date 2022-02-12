package com.mika.WineApp.mappers;

import com.mika.WineApp.users.model.User;
import com.mika.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    UserDTO toUserDTO(User model);
}
