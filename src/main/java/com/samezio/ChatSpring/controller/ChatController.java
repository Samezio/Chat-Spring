package com.samezio.ChatSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.samezio.ChatSpring.data.services.ChatHistoryService;
import com.samezio.ChatSpring.pojo.ChatMessage;

@Controller
public class ChatController {
    @Autowired
    private ChatHistoryService chatHistoryService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        chatHistoryService.insertChat(message.getSender(), message.getMessageType(), null);
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor accessor) {
        accessor.getSessionAttributes().put("username", message.getSender());
        chatHistoryService.insertChat(message.getSender(), message.getMessageType(), message.getContent());
        return message;
    }
}
