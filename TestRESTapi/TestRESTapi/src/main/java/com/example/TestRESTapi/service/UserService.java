package com.example.TestRESTapi.service;

import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.mappery.UserMappingStrategy;
import com.example.TestRESTapi.model.UserModel;
import com.example.TestRESTapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMappingStrategy mappingStrategy;

    public UserService(UserRepository userRepository, UserMappingStrategy mappingStrategy) {
        this.userRepository = userRepository;
        this.mappingStrategy = mappingStrategy;
    }

    public List<ResponseUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mappingStrategy::toDto)
                .collect(Collectors.toList());
    }

    public List<ResponseUserDTO> getUsers(int count) {
        return userRepository.findFirstNUsers(count).stream()
                .map(mappingStrategy::toDto)
                .toList();
    }

    public ResponseUserDTO createUser(ResponseUserDTO userDto) {
        UserModel model = mappingStrategy.toModel(userDto);
        return mappingStrategy.toDto(userRepository.save(model));
    }
}
