package com.example.TestRESTapi.mappery;

import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.model.UserModel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mapstruct")
public class MapStructUserMapper implements UserMappingStrategy {
    private final UserMapper userMapper;

    public MapStructUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ResponseUserDTO toDto(UserModel model) {
        return userMapper.toDto(model);
    }

    @Override
    public UserModel toModel(ResponseUserDTO dto) {
        return userMapper.toModel(dto);
    }
}
