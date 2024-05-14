package com.samezio.ChatSpring.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.samezio.ChatSpring.services.WebSocketAuthenticatorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthenticationSecurityConfig implements WebSocketMessageBrokerConfigurer {
    private static final String USERNAME_HEADER = "login";
    private static final String PASSWORD_HEADER = "passcode";
    @Autowired
    private WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            @Nullable
            public Message<?> preSend(Message<?> message, MessageChannel channel) throws AuthenticationException {
                // return ChannelInterceptor.super.preSend(message, channel);
                final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
                        StompHeaderAccessor.class);
                log.info("Command: {}", accessor.getCommand());
                if (StompCommand.CONNECT == accessor.getCommand()) {
                    final String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
                    final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);

                    final UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService
                            .getAuthenticatedOrFail(username, password);

                    accessor.setUser(user);
                }
                return message;
            }
        });
    }
}
