package com.samezio.ChatSpring.data.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.samezio.ChatSpring.data.entities.User;

public interface UserRepository extends MongoRepository<User, String> {

}
