package com.digiSchool.digiSchool.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.auth.service.JwtTokenService;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenService jwtTokenService;

    public WebSocketAuthChannelInterceptor(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtTokenService.validateToken(token)) {
                    Long userId = jwtTokenService.extractUserId(token);
                    String email = jwtTokenService.extractUsername(token);
                    String role = jwtTokenService.extractRole(token);
                    String tenantId = jwtTokenService.extractTenantId(token);

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    if (role != null) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                    }

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);
                    accessor.setUser(auth);

                    // Store user info in session attributes
                    accessor.getSessionAttributes().put("userId", userId);
                    accessor.getSessionAttributes().put("email", email);
                    accessor.getSessionAttributes().put("role", role);
                    accessor.getSessionAttributes().put("tenantId", tenantId);

                    // Extract name from token claims if available, fallback to email
                    accessor.getSessionAttributes().put("name", email);
                }
            }
        }

        return message;
    }
}
