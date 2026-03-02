package com.digiSchool.digiSchool.auth.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.digiSchool.digiSchool.auth.service.PresenceService;

@Controller
public class PresenceStompController {

    private final PresenceService presenceService;

    public PresenceStompController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @MessageMapping("/presence/heartbeat")
    public void heartbeat(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null) {
            presenceService.refreshSession(sessionId);
        }
    }
}
