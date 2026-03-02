package com.digiSchool.digiSchool.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.digiSchool.digiSchool.auth.service.PresenceService;

@Component
public class WebSocketEventListener {

    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final PresenceService presenceService;

    public WebSocketEventListener(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        if (sessionAttributes == null) return;

        Long userId = (Long) sessionAttributes.get("userId");
        String email = (String) sessionAttributes.get("email");
        String name = (String) sessionAttributes.get("name");
        String role = (String) sessionAttributes.get("role");
        String tenantId = (String) sessionAttributes.get("tenantId");
        String sessionId = accessor.getSessionId();

        if (userId == null || sessionId == null) return;

        // Extract User-Agent and IP from session attributes (set by HandshakeInterceptor)
        String userAgent = (String) sessionAttributes.get("userAgent");
        if (userAgent == null) userAgent = "Unknown";

        String ip = (String) sessionAttributes.get("ip");
        if (ip == null) ip = "unknown";

        log.info("WebSocket CONNECT: userId={}, email={}, sessionId={}", userId, email, sessionId);

        presenceService.userConnected(sessionId, userId, email, name, role, tenantId, userAgent, ip);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        if (sessionId == null) return;

        // Extract userId from session attributes as fallback
        // This is critical because Redis session data may have already expired
        Long userId = null;
        String email = null;
        String name = null;
        String role = null;

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes != null) {
            userId = (Long) sessionAttributes.get("userId");
            email = (String) sessionAttributes.get("email");
            name = (String) sessionAttributes.get("name");
            role = (String) sessionAttributes.get("role");
        }

        log.info("WebSocket DISCONNECT: sessionId={}, userId={}, email={}", sessionId, userId, email);

        presenceService.userDisconnected(sessionId, userId, email, name, role);
    }
}