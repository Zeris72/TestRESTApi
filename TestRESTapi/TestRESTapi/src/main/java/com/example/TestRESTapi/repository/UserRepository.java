package com.example.TestRESTapi.repository;

import com.example.TestRESTapi.model.UserModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<UserModel,String> {
    default List<UserModel> findFirstNUsers(int count) {
        return findAll(PageRequest.of(0, count)).getContent();
    }
}
