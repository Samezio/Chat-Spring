package com.samezio.ChatSpring.services;

import java.util.Collection;

import com.samezio.ChatSpring.pojo.ChatMessage;

public interface ChatStorageService {
    void store(ChatMessage chatMessage);
    Collection<ChatMessage> getAllChat();
}
