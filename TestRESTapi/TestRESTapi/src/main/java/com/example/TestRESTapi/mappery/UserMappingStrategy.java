package com.example.TestRESTapi.mappery;

import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.model.UserModel;

public interface UserMappingStrategy {
    ResponseUserDTO toDto(UserModel model);
    UserModel toModel(ResponseUserDTO dto);
}
