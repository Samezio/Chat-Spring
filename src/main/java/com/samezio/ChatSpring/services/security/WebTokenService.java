package com.samezio.ChatSpring.services.security;

public interface WebTokenService {
    String getWebToken(String user);
    void removeWebToken(String user);
    String generateNewToken(String user); 
}
