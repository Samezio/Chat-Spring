package com.samezio.ChatSpring.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType messageType;
}
