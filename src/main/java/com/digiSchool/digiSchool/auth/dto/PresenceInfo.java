package com.digiSchool.digiSchool.auth.dto;

import java.time.LocalDateTime;

public class PresenceInfo {

    private Long userId;
    private String userEmail;
    private String userName;
    private String role;
    private String tenantId;
    private String status; // ONLINE / OFFLINE
    private int activeSessions;
    private String device;
    private String browser;
    private String os;
    private String ip;
    private LocalDateTime connectedAt;
    private LocalDateTime disconnectedAt;
    private LocalDateTime lastSeenAt;
    private String sessionDuration; // e.g. "2h 15m 30s"
    private long sessionDurationSeconds; // duration in seconds for programmatic use

    public PresenceInfo() {}

    public PresenceInfo(Long userId, String userEmail, String userName, String role,
                        String tenantId, String status, int activeSessions) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.role = role;
        this.tenantId = tenantId;
        this.status = status;
        this.activeSessions = activeSessions;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getActiveSessions() { return activeSessions; }
    public void setActiveSessions(int activeSessions) { this.activeSessions = activeSessions; }

    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }

    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }

    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

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