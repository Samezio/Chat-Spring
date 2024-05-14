package com.samezio.ChatSpring.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.samezio.ChatSpring.services.UsersService;
import com.samezio.ChatSpring.services.security.WebTokenService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UsersService userRepository;
    @Autowired
    private WebTokenService webTokenService;

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
    public String login(@RequestBody(required = true) LoginJSON loginJson) {
        log.info("Login {}", loginJson);
        if(userRepository.matchCredentials(loginJson.getUsername(), loginJson.getPassword())) {
            return webTokenService.generateNewToken(loginJson.getUsername());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
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
