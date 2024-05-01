package com.samezio.ChatSpring.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samezio.ChatSpring.data.entities.ChatHistory;
import com.samezio.ChatSpring.data.repositories.ChatHistoryRepository;
import com.samezio.ChatSpring.pojo.MessageType;

import lombok.NonNull;

@Service
public class ChatHistoryService {
    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    public void insertChat(@NonNull String sender, @NonNull MessageType messageType, String message) {
        final ChatHistory chatHistory = ChatHistory.builder().sender(sender).messageType(messageType).message(message)
                .build();
        chatHistoryRepository.save(chatHistory);
    }
}
