package com.samezio.ChatSpring.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.samezio.ChatSpring.services.security.WebTokenService;

@Service
public class WebSocketAuthenticatorService {
    @Autowired
    private WebTokenService webTokenService;
    @Autowired
    private UsersService usersService;

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String username, final String password)
            throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }

        if (!usersService.isExist(username)) {
            throw new BadCredentialsException("Bad credentials for user " + username);
        }
        if (!password.equalsIgnoreCase(webTokenService.getWebToken(username))) {
            throw new BadCredentialsException("Bad credentials for user " + username);
        }

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }
}
