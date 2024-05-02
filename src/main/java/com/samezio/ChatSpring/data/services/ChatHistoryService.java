package com.samezio.ChatSpring.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samezio.ChatSpring.data.entities.ChatHistory;
import com.samezio.ChatSpring.data.repositories.ChatHistoryRepository;
import com.samezio.ChatSpring.pojo.MessageType;

import lombok.NonNull;

@Service
public class ChatHistoryService {
    private final static String SEQUENCE_NAME = "chat_history_sequence";
    @Autowired
    private ChatHistoryRepository chatHistoryRepository;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public void insertChat(@NonNull String sender, @NonNull MessageType messageType, String message) {
        final ChatHistory chatHistory = ChatHistory.builder()
                .sequence(sequenceGeneratorService.generateSequence(SEQUENCE_NAME))
                .sender(sender).messageType(messageType).message(message)
                .build();
        chatHistoryRepository.save(chatHistory);
    }
}
