package com.samezio.ChatSpring.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.samezio.ChatSpring.data.entities.User;
import com.samezio.ChatSpring.data.repositories.UserRepository;

import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserRepository userRepository;

    @PostMapping("/register")
    public void register(@RequestBody UserRegisterJSON userRegisterJSON) {
        if (isUsernameValid(userRegisterJSON.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't register as username already used.");
        }
        User user = User.builder()
                .username(userRegisterJSON.getUsername().trim().toUpperCase())
                .email(userRegisterJSON.getEmail())
                .password(userRegisterJSON.getPassword()).build();
        userRepository.save(user);
    }

    @PostMapping("/new/{username}/valid")
    public boolean isUsernameValid(@PathVariable(required = true) String username) {
        return userRepository.findById(username.trim().toUpperCase()).isPresent();
    }

    @Data
    public static class UserRegisterJSON {
        private String username, email, password;
    }
}
