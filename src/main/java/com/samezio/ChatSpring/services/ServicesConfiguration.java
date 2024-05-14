package com.samezio.ChatSpring.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samezio.ChatSpring.pojo.ChatMessage;
import com.samezio.ChatSpring.pojo.MessageType;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ServicesConfiguration {

    @Autowired
    @Bean
    public UsersService fileBasedUsersService(@Value("${user-file}") Path usersFile) throws IOException {
        if(!Files.isRegularFile(usersFile)) {
            throw new FileNotFoundException("users file not found");
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(usersFile.toFile()))) {
            String line = reader.readLine();
            final Map<String, String> userMap = new HashMap<>();
            while(line!= null) {
                Optional.of(line.split(",")).filter(s->s.length == 2).map(s->{
                    s[0] = s[0].trim().toUpperCase();
                    s[1] = s[1].trim();
                    return s;
                }).ifPresent(s->userMap.put(s[0], s[1]));
                line = reader.readLine();
            }
            return new UsersService() {

                @Override
                public boolean matchCredentials(String user, String password) {
                    if(user == null || password == null)
                        return false;
                    user = user.trim().toUpperCase();
                    return password.equals(userMap.get(user));
                }
    
                @Override
                public boolean isExist(String user) {
                    if(user == null)
                        return false;
                    user = user.trim().toUpperCase();
                    return userMap.containsKey(user);
                }
    
                @Override
                public Collection<String> getUsers() {
                    return userMap.keySet();
                }

                @Override
                public void registerUser(String user, String password) {
                    userMap.put(user.trim().toUpperCase(), password);
                }
                
            };
        }
    }
    
    public ChatStorageService fileBasedChatStorageService(@Value("${chat-file}") Path chatsFile) throws IOException {
        if(!Files.isRegularFile(chatsFile)) {
            Files.createDirectories(chatsFile);
            Files.createFile(chatsFile);
        }
        @Data
        class FileBasedChatStorageService implements ChatStorageService, AutoCloseable {
            private final FileWriter writer;
            @Override
            public void close() throws Exception {
                writer.close();
            }

            @Override
            public void store(ChatMessage chatMessage) {
                try {
                    writer.write(String.format("%s, %s, %s", chatMessage.getMessageType().toString(), chatMessage.getSender(), chatMessage.getContent() == null ? "": chatMessage.getContent()));
                } catch (IOException e) {
                    log.warn("Unalbe to persist chat message", e);
                }
            }

            @Override
            public Collection<ChatMessage> getAllChat() {
                try(BufferedReader reader = new BufferedReader(new FileReader(chatsFile.toFile()))) {
                    Collection<ChatMessage> chatMessages = new HashSet<>();
                    String line = reader.readLine();
                    while (line != null) {
                        line = line.trim();
                        Optional.of(line.split(",")).filter(s->s.length == 3)
                            .map(s->{
                                return new ChatMessage(s[2].trim(), s[0].trim(), MessageType.valueOf(s[1].trim()));
                            })
                            .ifPresent(chatMessages::add);
                    }
                    return chatMessages;
                } catch (IOException e) {
                    log.warn("Unable to read stored chat message", e);
                    return Collections.emptySet();
                }
            }

        }
        return new FileBasedChatStorageService(new FileWriter(chatsFile.toFile()));
    }
}
