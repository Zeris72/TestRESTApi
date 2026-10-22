package com.example.TestRESTapi.service;

import com.example.TestRESTapi.DTO.RequestUserDTO;
import com.example.TestRESTapi.model.Address;
import com.example.TestRESTapi.model.UserModel;
import com.example.TestRESTapi.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserGenerationService {
    private final UserRepository userRepository;
    private final Faker faker = new Faker();

    public UserGenerationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUserFromDto(RequestUserDTO dto) {
        UserModel user = new UserModel();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setBirthDate(dto.getBirthDate());
        user.setAddress(dto.getAddress());
        user.setActive(dto.isActive());
        user.setRoles(dto.getRoles());
        user.setCreatedAt(dto.getCreatedAt());
        userRepository.save(user);
    }
    public void generateUsers(int count) {
        for (int i = 0; i < count; i++) {
            RequestUserDTO user = new RequestUserDTO();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            user.setBirthDate(faker.date().birthday());
            Address address = new Address();
            address.setCity(faker.address().city());
            address.setStreet(faker.address().streetName());
            address.setZip(faker.address().zipCode());
            user.setAddress(address);
            user.setActive(faker.bool().bool());
            List<String> roles = new ArrayList<>();
            roles.add(faker.hobbit().character());
            roles.add(faker.hobbit().character());
            user.setRoles(roles);
            user.setCreatedAt(faker.date().past(10, TimeUnit.HOURS));
            saveUserFromDto(user);
        }
    }

    public List<UserModel> generateUsersNoDB(int count) {
        List<UserModel> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserModel user = new UserModel();

            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            user.setBirthDate(faker.date().birthday());

            Address address = new Address();
            address.setCity(faker.address().city());
            address.setStreet(faker.address().streetName());
            address.setZip(faker.address().zipCode());
            user.setAddress(address);

            user.setActive(faker.bool().bool());

            List<String> roles = new ArrayList<>();
            roles.add(faker.hobbit().character());  // Dodaje rolę z hobbitów
            roles.add(faker.hobbit().character());  // Możesz dodać więcej ról według potrzeby
            user.setRoles(roles);

            user.setCreatedAt(faker.date().past(10, TimeUnit.HOURS));
            users.add(user);
        }
        return users;
    }
}
