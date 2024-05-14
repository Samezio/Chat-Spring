package com.samezio.ChatSpring.services;

import java.util.Collection;

public interface UsersService {
    boolean matchCredentials(String user, String password);
    boolean isExist(String user);
    Collection<String> getUsers();
    void registerUser(String user, String password);
}
