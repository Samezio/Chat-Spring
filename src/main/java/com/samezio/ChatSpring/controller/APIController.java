package com.samezio.ChatSpring.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.samezio.ChatSpring.data.entities.ChatHistory;
import com.samezio.ChatSpring.data.repositories.ChatHistoryRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api")
public class APIController {
    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @GetMapping("/history")
    @ResponseBody
    public Collection<ChatHistory> getHistory() {
        List<ChatHistory> history = chatHistoryRepository.findAll();
        log.info("history: {}", history);
        return history;
    }
}
