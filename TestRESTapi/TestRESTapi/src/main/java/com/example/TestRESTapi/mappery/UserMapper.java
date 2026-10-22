package com.example.TestRESTapi.mappery;

import ch.qos.logback.core.model.ComponentModel;
import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.model.UserModel;
import org.mapstruct.Mapper;


//To jest do MapStruct
@Mapper(componentModel="spring")
public interface UserMapper {
    ResponseUserDTO toDto(UserModel model);
    UserModel toModel(ResponseUserDTO dto);
}
