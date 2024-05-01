package com.samezio.ChatSpring.data.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.samezio.ChatSpring.pojo.MessageType;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document("chat_history")
public class ChatHistory {

    @Transient
    public static final String SEQUENCE_NAME = "chat_history_sequence";

    @Id
    private long sequence;

    private String sender;
    private MessageType messageType;
    private String message;
}
