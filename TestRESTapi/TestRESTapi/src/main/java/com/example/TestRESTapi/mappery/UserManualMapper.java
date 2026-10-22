package com.example.TestRESTapi.mappery;

import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.model.UserModel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("manual")
public class UserManualMapper implements UserMappingStrategy {

    @Override
    public ResponseUserDTO toDto(UserModel model) {
        ResponseUserDTO dto = new ResponseUserDTO();
        dto.setId(model.getId());
        dto.setFirstName(model.getFirstName());
        dto.setLastName(model.getLastName());
        dto.setEmail(model.getEmail());
        dto.setBirthDate(model.getBirthDate());
        dto.setAddress(model.getAddress());
        dto.setActive(model.isActive());
        dto.setRoles(model.getRoles());
        dto.setCreatedAt(model.getCreatedAt());
        return dto;
    }

    @Override
    public UserModel toModel(ResponseUserDTO dto) {
        UserModel model = new UserModel();
        model.setId(dto.getId());
        model.setFirstName(dto.getFirstName());
        model.setLastName(dto.getLastName());
        model.setEmail(dto.getEmail());
        model.setBirthDate(dto.getBirthDate());
        model.setAddress(dto.getAddress());
        model.setActive(dto.isActive());
        model.setRoles(dto.getRoles());
        model.setCreatedAt(dto.getCreatedAt());
        return model;
    }
}
