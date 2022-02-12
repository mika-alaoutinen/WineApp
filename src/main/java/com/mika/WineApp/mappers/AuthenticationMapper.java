package com.mika.WineApp.mappers;

import com.mika.WineApp.models.JwtToken;
import com.mika.WineApp.models.UserCredentials;
import com.mika.model.JwtTokenDTO;
import com.mika.model.UserCredentialsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    JwtTokenDTO toTokenDTO(JwtToken model);

    UserCredentials toCredentials(UserCredentialsDTO dto);
}
