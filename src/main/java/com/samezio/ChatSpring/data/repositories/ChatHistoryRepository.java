package com.samezio.ChatSpring.data.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.samezio.ChatSpring.data.entities.ChatHistory;

public interface ChatHistoryRepository extends MongoRepository<ChatHistory, Long> {

}
