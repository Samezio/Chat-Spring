package com.samezio.ChatSpring.services.securiy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(t -> t
                        .disable())
                .sessionManagement(t -> t.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .httpBasic(t -> t.disable())
                .authorizeHttpRequests(t -> t.requestMatchers("/stomp").permitAll().anyRequest().permitAll());
        return http.build();
    }
}