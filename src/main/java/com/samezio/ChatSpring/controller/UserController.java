package com.samezio.ChatSpring.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.samezio.ChatSpring.services.UsersService;

import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user")
public class UserController {
    private UsersService userRepository;

    @PostMapping("/register")
    public void register(@RequestBody UserRegisterJSON userRegisterJSON) {
        if (isUsernameValid(userRegisterJSON.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't register as username already used.");
        }
        userRepository.registerUser(userRegisterJSON.getUsername(), userRegisterJSON.getPassword());
    }

    @PostMapping("/new/{username}/valid")
    public boolean isUsernameValid(@PathVariable(required = true) String username) {
        return userRepository.isExist(username);
    }

    @PostMapping("/login")
    public boolean login(@PathVariable(required = true) LoginJSON loginJson) {
        return userRepository.matchCredentials(loginJson.getUsername(), loginJson.getPassword());
    }

    @Data
    public static class UserRegisterJSON {
        private String username, email, password;
    }

    @Data
    public static class LoginJSON {
        private String username, password;
    }
}
