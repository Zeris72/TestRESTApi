package com.example.TestRESTapi.mappery;

import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.model.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("modelmapper")
public class ModelMapperUserMapper implements UserMappingStrategy {
    private final ModelMapper modelMapper;

    public ModelMapperUserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseUserDTO toDto(UserModel model) {
        return modelMapper.map(model, ResponseUserDTO.class);
    }

    @Override
    public UserModel toModel(ResponseUserDTO dto) {
        return modelMapper.map(dto, UserModel.class);
    }
}
