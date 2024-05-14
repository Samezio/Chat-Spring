package com.samezio.ChatSpring.services.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.samezio.ChatSpring.services.security.WebTokenAuthenticationProvider.WebTokenAuthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
public class WebSecurityConfiguration {
        @Autowired
        private ApplicationContext applicationContext;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(t -> t
                                                .disable())
                                .sessionManagement(t -> t.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .httpBasic(t -> t.disable())
                                .addFilterAfter(new WebTokenAuthenticationFilter(applicationContext),
                                                BasicAuthenticationFilter.class)
                                .authorizeHttpRequests(
                                                t -> t.requestMatchers("/stomp", "/ws", "/ws/**").permitAll()
                                                                .requestMatchers("/user/login", "/user/register",
                                                                                "/user/new/*/valid")
                                                                .permitAll().anyRequest().authenticated());
                return http.build();
        }

        @Autowired
        @Bean(name = "webTokenAuthenticationProvider")
        public AuthenticationProvider webTokenAuthenticationProvider(
                        @Qualifier("webTokenUserDetailsService") UserDetailsService userDetailsService) {
                return new WebTokenAuthenticationProvider(userDetailsService);
        }

        @Bean(name = "unauthenticatedWebTokenPrototype")
        @Scope("prototype")
        public WebTokenAuthentication unauthenticatedWebTokenPrototype() {
                return new WebTokenAuthentication(false);
        }

        @Bean
        public WebTokenService webTokenService() {
                return new WebTokenService() {
                        private final Map<String, String> tokenMap = new HashMap<>();
                        @Override
                        public String getWebToken(String user) {
                                if(user == null) return null;
                                return tokenMap.get(user.trim().toUpperCase());
                        }

                        @Override
                        public void removeWebToken(String user) {
                                tokenMap.remove(user.trim().toUpperCase());
                        }

                        @Override
                        public String generateNewToken(String user) {
                                String token = UUID.randomUUID().toString();
                                while(tokenMap.containsValue(token)) {
                                        token = UUID.randomUUID().toString();
                                }
                                tokenMap.put(user, token);
                                return token;
                        }
                        
                };
        }

}

@RequiredArgsConstructor
class WebTokenAuthenticationFilter extends OncePerRequestFilter {
        private final ApplicationContext applicationContext;

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
                return new OrRequestMatcher(new AntPathRequestMatcher("/ws/**"), 
                                new AntPathRequestMatcher("/stomp"),
                                new AntPathRequestMatcher("/user/login"),
                                new AntPathRequestMatcher("/user/register"),
                                new AntPathRequestMatcher("/user/new/*/valid"), new AntPathRequestMatcher("/*", HttpMethod.OPTIONS.name())).matches(request);
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {
                String[] tokenHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                                .map(header -> header.split(" "))
                                .filter(tokenSplit -> tokenSplit.length == 2)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Authorization header missing or invalid: " + request.getRequestURI()));
                final String username = tokenHeader[0];
                final String token = tokenHeader[1];
                WebTokenAuthentication webTokenAuthentication = applicationContext
                                .getBean("unauthenticatedWebTokenPrototype", WebTokenAuthentication.class);
                webTokenAuthentication.setUsername(username.trim().toUpperCase());
                webTokenAuthentication.setToken(token);
                Authentication authentication = applicationContext
                                .getBean("webTokenAuthenticationProvider", AuthenticationProvider.class)
                                .authenticate(webTokenAuthentication);
                if (authentication.isAuthenticated()) {
                        SecurityContext securityContext = SecurityContextHolder.getContext();
                        securityContext.setAuthentication(authentication);
                        filterChain.doFilter(request, response);
                } else {
                        throw new BadCredentialsException("Invalid authentication params");
                }
        }

}