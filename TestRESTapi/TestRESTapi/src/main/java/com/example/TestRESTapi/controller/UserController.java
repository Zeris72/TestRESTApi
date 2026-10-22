package com.example.TestRESTapi.controller;

import com.example.TestRESTapi.DTO.ResponseUserDTO;
import com.example.TestRESTapi.mappery.UserMappingStrategy;
import com.example.TestRESTapi.model.UserModel;
import com.example.TestRESTapi.service.UserGenerationService;
import com.example.TestRESTapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserGenerationService userGenerationService;
    private final UserMappingStrategy userMappingStrategy;

    public UserController(UserService userService,UserGenerationService userGenerationService,UserMappingStrategy userMappingStrategy) {

        this.userService = userService;
        this.userGenerationService = userGenerationService;
        this.userMappingStrategy = userMappingStrategy;
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{count}")
    public ResponseEntity<List<ResponseUserDTO>> getUsers(@PathVariable int count) {
        List<ResponseUserDTO> users = userService.getUsers(count);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/test-users/{count}")
    public ResponseEntity<List<ResponseUserDTO>> testUsers(@PathVariable int count) {
        List<UserModel> models = userGenerationService.generateUsersNoDB(count);
        List<ResponseUserDTO> dtos = models.stream()
                .map(userMappingStrategy::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/generate/{count}")
    public String generateUsers(@PathVariable int count) {
        userGenerationService.generateUsers(count);
        return count + " users generated.";
    }
}
