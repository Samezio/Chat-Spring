package com.samezio.ChatSpring.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.samezio.ChatSpring.pojo.ChatMessage;
import com.samezio.ChatSpring.services.ChatStorageService;

import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class APIController {
    @Autowired
    private ChatStorageService chatHistoryRepository;

    @GetMapping("/history")
    @ResponseBody
    public Collection<ChatMessage> getHistory() {
        return chatHistoryRepository.getAllChat();
    }
}
