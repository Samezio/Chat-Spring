package com.samezio.ChatSpring.services.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.samezio.ChatSpring.services.UsersService;

import lombok.RequiredArgsConstructor;

@Configuration
public class UserDetailsServiceFactory {
    @Autowired
    private UsersService userRepository;
    @Autowired
    private WebTokenService webTokenService;

    @RequiredArgsConstructor
    class WebTokenUserDetails implements UserDetails {
        public static final Collection<? extends GrantedAuthority> AUTHORITY = Collections
                .singleton(new SimpleGrantedAuthority("USER"));
        private final String user, token;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AUTHORITY;
        }

        @Override
        public String getPassword() {
            return token;
        }

        @Override
        public String getUsername() {
            return user;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;

        }

        @Override
        public boolean isEnabled() {
            return true;
        }

    }

    @Bean(name = "webTokenUserDetailsService")
    public UserDetailsService webTokenUserDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if (username == null) {
                    return null;
                }
                username = username.trim().toUpperCase();
                if(!userRepository.isExist(username)) return null;
                if(webTokenService.getWebToken(username) == null) return null;
                return new WebTokenUserDetails(username, webTokenService.getWebToken(username));
            }

        };
    }
}
