package com.digiSchool.digiSchool.config;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Intercepte le handshake HTTP du WebSocket pour extraire le User-Agent et l'IP.
 * Ces informations ne sont pas disponibles dans les headers STOMP.
 */
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            String userAgent = httpRequest.getHeader("User-Agent");
            if (userAgent != null) {
                attributes.put("userAgent", userAgent);
            }

            // Try X-Forwarded-For first (behind proxy/load balancer), then remote addr
            String ip = httpRequest.getHeader("X-Forwarded-For");
            if (ip != null && !ip.isBlank()) {
                // X-Forwarded-For can contain multiple IPs, take the first one
                ip = ip.split(",")[0].trim();
            } else {
                ip = httpRequest.getRemoteAddr();
            }
            attributes.put("ip", ip);
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Nothing to do
    }
}