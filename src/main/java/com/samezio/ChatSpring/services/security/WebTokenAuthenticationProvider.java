package com.samezio.ChatSpring.services.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebTokenAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        if (userDetails == null) {
            return null;
        }
        if (!userDetails.getPassword().equals(authentication.getCredentials())) {
            return null;
        }
        return new WebTokenAuthentication(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(WebTokenAuthentication.class);
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class WebTokenAuthentication implements Authentication {
        private String token;
        private String username;
        private final boolean authenticated;

        private WebTokenAuthentication(Authentication authentication) {
            this.authenticated = true;
            this.username = authentication.getName();
            this.token = authentication.getCredentials().toString();
        }

        @Override
        public String getName() {
            return username;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singleton(new SimpleGrantedAuthority("USER"));
        }

        @Override
        public Object getCredentials() {
            return token;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return username;
        }

        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            throw new UnsupportedOperationException("Unimplemented method 'setAuthenticated'");
        }

    }
}
