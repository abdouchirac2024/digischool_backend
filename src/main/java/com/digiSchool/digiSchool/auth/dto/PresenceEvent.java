package com.digiSchool.digiSchool.auth.dto;

import java.time.LocalDateTime;

public class PresenceEvent {

    public enum EventType {
        USER_ONLINE,
        USER_OFFLINE,
        PRESENCE_UPDATE
    }

    private EventType type;
    private Long userId;
    private String userEmail;
    private String userName;
    private String role;
    private String device;
    private String browser;
    private String os;
    private String ip;
    private String status; // ONLINE / OFFLINE
    private long onlineCount;
    private LocalDateTime timestamp;
    private LocalDateTime connectedAt;
    private LocalDateTime disconnectedAt;
    private LocalDateTime lastSeenAt;
    private String sessionDuration; // e.g. "2h 15m 30s"
    private long sessionDurationSeconds;

    public PresenceEvent() {}

    public PresenceEvent(EventType type, Long userId, String userEmail, String userName,
                         String status, long onlineCount) {
        this.type = type;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.status = status;
        this.onlineCount = onlineCount;
        this.timestamp = LocalDateTime.now();
    }

    public EventType getType() { return type; }
    public void setType(EventType type) { this.type = type; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }

    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }

    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getOnlineCount() { return onlineCount; }
    public void setOnlineCount(long onlineCount) { this.onlineCount = onlineCount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public LocalDateTime getConnectedAt() { return connectedAt; }
    public void setConnectedAt(LocalDateTime connectedAt) { this.connectedAt = connectedAt; }

    public LocalDateTime getDisconnectedAt() { return disconnectedAt; }
    public void setDisconnectedAt(LocalDateTime disconnectedAt) { this.disconnectedAt = disconnectedAt; }

    public LocalDateTime getLastSeenAt() { return lastSeenAt; }
    public void setLastSeenAt(LocalDateTime lastSeenAt) { this.lastSeenAt = lastSeenAt; }

    public String getSessionDuration() { return sessionDuration; }
    public void setSessionDuration(String sessionDuration) { this.sessionDuration = sessionDuration; }

    public long getSessionDurationSeconds() { return sessionDurationSeconds; }
    public void setSessionDurationSeconds(long sessionDurationSeconds) { this.sessionDurationSeconds = sessionDurationSeconds; }
}