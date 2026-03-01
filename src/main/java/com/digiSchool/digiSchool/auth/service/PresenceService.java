package com.digiSchool.digiSchool.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.auth.dto.PresenceEvent;
import com.digiSchool.digiSchool.auth.dto.PresenceInfo;

@Service
public class PresenceService {

    private static final Logger log = LoggerFactory.getLogger(PresenceService.class);

    private static final String SESSION_KEY_PREFIX = "presence:session:";
    private static final String USER_KEY_PREFIX = "presence:user:";
    private static final String ONLINE_SET_KEY = "presence:online";
    private static final String USER_SESSIONS_PREFIX = "presence:sessions:";
    // Reverse mapping: sessionId -> userId (survives session TTL expiry)
    private static final String SESSION_USER_PREFIX = "presence:sessionuser:";

    private static final Duration SESSION_TTL = Duration.ofMinutes(5);
    private static final Duration USER_TTL = Duration.ofHours(24);
    private static final Duration USER_SESSIONS_TTL = Duration.ofMinutes(10);
    // Session-user mapping lives longer than session data to survive TTL expiry
    private static final Duration SESSION_USER_TTL = Duration.ofHours(1);

    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public PresenceService(StringRedisTemplate redisTemplate, SimpMessagingTemplate messagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    public void userConnected(String sessionId, Long userId, String email, String name,
                              String role, String tenantId, String userAgent, String ip) {
        String userIdStr = userId.toString();
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        String userKey = USER_KEY_PREFIX + userIdStr;
        String userSessionsKey = USER_SESSIONS_PREFIX + userIdStr;
        String sessionUserKey = SESSION_USER_PREFIX + sessionId;
        LocalDateTime now = LocalDateTime.now();

        // Parse User-Agent
        String browser = parseBrowser(userAgent);
        String os = parseOs(userAgent);
        String device = browser + " / " + os;

        // Store session data in Redis
        redisTemplate.opsForHash().put(sessionKey, "userId", userIdStr);
        redisTemplate.opsForHash().put(sessionKey, "email", email);
        redisTemplate.opsForHash().put(sessionKey, "device", device);
        redisTemplate.opsForHash().put(sessionKey, "browser", browser);
        redisTemplate.opsForHash().put(sessionKey, "os", os);
        redisTemplate.opsForHash().put(sessionKey, "ip", ip);
        redisTemplate.opsForHash().put(sessionKey, "connectedAt", now.toString());
        redisTemplate.expire(sessionKey, SESSION_TTL);

        // Store reverse mapping sessionId -> userId (with longer TTL)
        // This ensures we can find the userId even if session data expires
        redisTemplate.opsForValue().set(sessionUserKey, userIdStr, SESSION_USER_TTL);

        // Add session to user's session set
        redisTemplate.opsForSet().add(userSessionsKey, sessionId);
        redisTemplate.expire(userSessionsKey, USER_SESSIONS_TTL);

        // Add user to online set
        redisTemplate.opsForSet().add(ONLINE_SET_KEY, userIdStr);

        // Check if user was already online (don't overwrite connectedAt)
        Object existingStatus = redisTemplate.opsForHash().get(userKey, "status");
        boolean wasAlreadyOnline = "ONLINE".equals(existingStatus != null ? existingStatus.toString() : "");

        // Update user presence info
        long activeSessions = redisTemplate.opsForSet().size(userSessionsKey);
        redisTemplate.opsForHash().put(userKey, "status", "ONLINE");
        redisTemplate.opsForHash().put(userKey, "activeSessions", String.valueOf(activeSessions));
        redisTemplate.opsForHash().put(userKey, "lastSeenAt", now.toString());
        redisTemplate.opsForHash().put(userKey, "lastDevice", device);
        redisTemplate.opsForHash().put(userKey, "lastBrowser", browser);
        redisTemplate.opsForHash().put(userKey, "lastOs", os);
        redisTemplate.opsForHash().put(userKey, "lastIp", ip);
        redisTemplate.opsForHash().put(userKey, "email", email);
        redisTemplate.opsForHash().put(userKey, "name", name != null ? name : email);
        redisTemplate.opsForHash().put(userKey, "role", role != null ? role : "");
        redisTemplate.opsForHash().put(userKey, "tenantId", tenantId != null ? tenantId : "");

        // Only set connectedAt if user was not already online
        if (!wasAlreadyOnline) {
            redisTemplate.opsForHash().put(userKey, "connectedAt", now.toString());
            // Clear disconnectedAt since user is back online
            redisTemplate.opsForHash().delete(userKey, "disconnectedAt");
        }

        redisTemplate.expire(userKey, USER_TTL);

        // Broadcast presence event
        long onlineCount = getOnlineCount();
        PresenceEvent event = new PresenceEvent(
                PresenceEvent.EventType.USER_ONLINE, userId, email,
                name != null ? name : email, "ONLINE", onlineCount);
        event.setDevice(device);
        event.setBrowser(browser);
        event.setOs(os);
        event.setIp(ip);
        event.setRole(role);
        event.setConnectedAt(now);
        messagingTemplate.convertAndSend("/topic/presence", event);

        log.info("User connected: userId={}, email={}, sessionId={}, device={}, activeSessions={}, onlineCount={}",
                userId, email, sessionId, device, activeSessions, onlineCount);
    }

    /**
     * Called when a WebSocket session disconnects.
     *
     * @param sessionId the STOMP session ID
     * @param fallbackUserId userId from session attributes (fallback if Redis session expired)
     * @param fallbackEmail email from session attributes
     * @param fallbackName name from session attributes
     * @param fallbackRole role from session attributes
     */
    public void userDisconnected(String sessionId, Long fallbackUserId, String fallbackEmail,
                                  String fallbackName, String fallbackRole) {
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        String sessionUserKey = SESSION_USER_PREFIX + sessionId;

        // Try to get userId from Redis session data first
        Object userIdObj = redisTemplate.opsForHash().get(sessionKey, "userId");
        String userIdStr;

        if (userIdObj != null) {
            userIdStr = userIdObj.toString();
        } else {
            // Session data expired in Redis - try reverse mapping
            String mappedUserId = redisTemplate.opsForValue().get(sessionUserKey);
            if (mappedUserId != null) {
                userIdStr = mappedUserId;
                log.info("Session data expired, using reverse mapping for sessionId={}, userId={}", sessionId, userIdStr);
            } else if (fallbackUserId != null) {
                // Use fallback from WebSocket session attributes
                userIdStr = fallbackUserId.toString();
                log.info("Using fallback userId from session attributes for sessionId={}, userId={}", sessionId, userIdStr);
            } else {
                log.warn("No session data found for sessionId={} (no Redis data, no mapping, no fallback)", sessionId);
                return;
            }
        }

        String userKey = USER_KEY_PREFIX + userIdStr;
        String userSessionsKey = USER_SESSIONS_PREFIX + userIdStr;

        // Get email/name from Redis or fallback
        Object emailObj = redisTemplate.opsForHash().get(sessionKey, "email");
        String email = emailObj != null ? emailObj.toString() :
                       (fallbackEmail != null ? fallbackEmail : "unknown");
        Object deviceObj = redisTemplate.opsForHash().get(sessionKey, "device");
        String device = deviceObj != null ? deviceObj.toString() : "";
        Object browserObj = redisTemplate.opsForHash().get(sessionKey, "browser");
        String browser = browserObj != null ? browserObj.toString() : "";
        Object osObj = redisTemplate.opsForHash().get(sessionKey, "os");
        String osVal = osObj != null ? osObj.toString() : "";

        // Remove session data
        redisTemplate.delete(sessionKey);
        redisTemplate.delete(sessionUserKey);
        redisTemplate.opsForSet().remove(userSessionsKey, sessionId);

        // Check remaining sessions
        Long remainingSessions = redisTemplate.opsForSet().size(userSessionsKey);
        LocalDateTime now = LocalDateTime.now();

        if (remainingSessions == null || remainingSessions == 0) {
            // No more sessions -> user goes OFFLINE
            redisTemplate.opsForSet().remove(ONLINE_SET_KEY, userIdStr);

            // Calculate session duration
            Object connectedAtObj = redisTemplate.opsForHash().get(userKey, "connectedAt");
            String sessionDuration = "";
            long sessionDurationSeconds = 0;
            if (connectedAtObj != null) {
                try {
                    LocalDateTime connectedAt = LocalDateTime.parse(connectedAtObj.toString());
                    sessionDurationSeconds = ChronoUnit.SECONDS.between(connectedAt, now);
                    sessionDuration = formatDuration(sessionDurationSeconds);
                } catch (Exception e) {
                    log.warn("Could not parse connectedAt for userId={}: {}", userIdStr, e.getMessage());
                }
            }

            // Update user presence in Redis
            redisTemplate.opsForHash().put(userKey, "status", "OFFLINE");
            redisTemplate.opsForHash().put(userKey, "activeSessions", "0");
            redisTemplate.opsForHash().put(userKey, "disconnectedAt", now.toString());
            redisTemplate.opsForHash().put(userKey, "lastSeenAt", now.toString());
            redisTemplate.opsForHash().put(userKey, "lastSessionDuration", sessionDuration);
            redisTemplate.opsForHash().put(userKey, "lastSessionDurationSeconds", String.valueOf(sessionDurationSeconds));

            // Store info from fallback if Redis user data is incomplete
            if (fallbackEmail != null) {
                Object existingEmail = redisTemplate.opsForHash().get(userKey, "email");
                if (existingEmail == null || "unknown".equals(existingEmail.toString())) {
                    redisTemplate.opsForHash().put(userKey, "email", fallbackEmail);
                }
            }
            if (fallbackName != null) {
                Object existingName = redisTemplate.opsForHash().get(userKey, "name");
                if (existingName == null) {
                    redisTemplate.opsForHash().put(userKey, "name", fallbackName);
                }
            }
            if (fallbackRole != null) {
                Object existingRole = redisTemplate.opsForHash().get(userKey, "role");
                if (existingRole == null || existingRole.toString().isEmpty()) {
                    redisTemplate.opsForHash().put(userKey, "role", fallbackRole);
                }
            }

            redisTemplate.expire(userKey, USER_TTL);

            // Broadcast offline event
            Long userId = Long.parseLong(userIdStr);
            Object nameObj = redisTemplate.opsForHash().get(userKey, "name");
            String name = nameObj != null ? nameObj.toString() : email;
            Object roleObj = redisTemplate.opsForHash().get(userKey, "role");
            String role = roleObj != null ? roleObj.toString() : (fallbackRole != null ? fallbackRole : "");

            long onlineCount = getOnlineCount();
            PresenceEvent event = new PresenceEvent(
                    PresenceEvent.EventType.USER_OFFLINE, userId, email,
                    name, "OFFLINE", onlineCount);
            event.setDisconnectedAt(now);
            event.setLastSeenAt(now);
            event.setDevice(device);
            event.setBrowser(browser);
            event.setOs(osVal);
            event.setRole(role);
            event.setSessionDuration(sessionDuration);
            event.setSessionDurationSeconds(sessionDurationSeconds);

            // Include connectedAt in the event
            if (connectedAtObj != null) {
                try {
                    event.setConnectedAt(LocalDateTime.parse(connectedAtObj.toString()));
                } catch (Exception ignored) {}
            }

            messagingTemplate.convertAndSend("/topic/presence", event);

            log.info("User went OFFLINE: userId={}, email={}, duration={}, onlineCount={}",
                    userIdStr, email, sessionDuration, onlineCount);
        } else {
            // Update active sessions count
            redisTemplate.opsForHash().put(userKey, "activeSessions", String.valueOf(remainingSessions));
            redisTemplate.opsForHash().put(userKey, "lastSeenAt", now.toString());

            log.info("Session closed but user still online: userId={}, remainingSessions={}", userIdStr, remainingSessions);
        }
    }

    public void refreshSession(String sessionId) {
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        String sessionUserKey = SESSION_USER_PREFIX + sessionId;

        // Check if session exists
        if (Boolean.TRUE.equals(redisTemplate.hasKey(sessionKey))) {
            redisTemplate.expire(sessionKey, SESSION_TTL);
            // Also refresh the reverse mapping
            redisTemplate.expire(sessionUserKey, SESSION_USER_TTL);

            // Also refresh the user sessions set TTL
            Object userIdObj = redisTemplate.opsForHash().get(sessionKey, "userId");
            if (userIdObj != null) {
                String userIdStr = userIdObj.toString();
                String userSessionsKey = USER_SESSIONS_PREFIX + userIdStr;
                redisTemplate.expire(userSessionsKey, USER_SESSIONS_TTL);

                // Update lastSeenAt
                String userKey = USER_KEY_PREFIX + userIdStr;
                redisTemplate.opsForHash().put(userKey, "lastSeenAt", LocalDateTime.now().toString());

                // Ensure user is in online set (recovery from stale state)
                redisTemplate.opsForSet().add(ONLINE_SET_KEY, userIdStr);
            }
        } else if (Boolean.TRUE.equals(redisTemplate.hasKey(sessionUserKey))) {
            // Session expired but reverse mapping exists - session is stale
            // Clean up the reverse mapping
            redisTemplate.delete(sessionUserKey);
        }
    }

    public void forceDisconnectUser(Long userId) {
        String userIdStr = userId.toString();
        String userSessionsKey = USER_SESSIONS_PREFIX + userIdStr;
        String userKey = USER_KEY_PREFIX + userIdStr;
        LocalDateTime now = LocalDateTime.now();

        // Calculate session duration before clearing
        Object connectedAtObj = redisTemplate.opsForHash().get(userKey, "connectedAt");
        String sessionDuration = "";
        long sessionDurationSeconds = 0;
        if (connectedAtObj != null) {
            try {
                LocalDateTime connectedAt = LocalDateTime.parse(connectedAtObj.toString());
                sessionDurationSeconds = ChronoUnit.SECONDS.between(connectedAt, now);
                sessionDuration = formatDuration(sessionDurationSeconds);
            } catch (Exception ignored) {}
        }

        // Get all session IDs for this user
        Set<String> sessionIds = redisTemplate.opsForSet().members(userSessionsKey);
        if (sessionIds != null) {
            for (String sessionId : sessionIds) {
                redisTemplate.delete(SESSION_KEY_PREFIX + sessionId);
                redisTemplate.delete(SESSION_USER_PREFIX + sessionId);
            }
        }

        // Remove user sessions set
        redisTemplate.delete(userSessionsKey);

        // Remove from online set
        redisTemplate.opsForSet().remove(ONLINE_SET_KEY, userIdStr);

        // Update user presence
        redisTemplate.opsForHash().put(userKey, "status", "OFFLINE");
        redisTemplate.opsForHash().put(userKey, "activeSessions", "0");
        redisTemplate.opsForHash().put(userKey, "disconnectedAt", now.toString());
        redisTemplate.opsForHash().put(userKey, "lastSeenAt", now.toString());
        redisTemplate.opsForHash().put(userKey, "lastSessionDuration", sessionDuration);
        redisTemplate.opsForHash().put(userKey, "lastSessionDurationSeconds", String.valueOf(sessionDurationSeconds));
        redisTemplate.expire(userKey, USER_TTL);

        // Broadcast offline event
        Object emailObj = redisTemplate.opsForHash().get(userKey, "email");
        String email = emailObj != null ? emailObj.toString() : "unknown";
        Object nameObj = redisTemplate.opsForHash().get(userKey, "name");
        String name = nameObj != null ? nameObj.toString() : email;
        Object roleObj = redisTemplate.opsForHash().get(userKey, "role");
        String role = roleObj != null ? roleObj.toString() : "";

        long onlineCount = getOnlineCount();
        PresenceEvent event = new PresenceEvent(
                PresenceEvent.EventType.USER_OFFLINE, userId, email,
                name, "OFFLINE", onlineCount);
        event.setDisconnectedAt(now);
        event.setLastSeenAt(now);
        event.setRole(role);
        event.setSessionDuration(sessionDuration);
        event.setSessionDurationSeconds(sessionDurationSeconds);
        messagingTemplate.convertAndSend("/topic/presence", event);

        log.info("Force disconnected user: userId={}, duration={}", userId, sessionDuration);
    }

    public Set<Long> getOnlineUserIds() {
        Set<String> members = redisTemplate.opsForSet().members(ONLINE_SET_KEY);
        if (members == null || members.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> ids = new HashSet<>();
        for (String m : members) {
            try {
                ids.add(Long.parseLong(m));
            } catch (NumberFormatException ignored) {}
        }
        return ids;
    }

    public long getOnlineCount() {
        Long size = redisTemplate.opsForSet().size(ONLINE_SET_KEY);
        return size != null ? size : 0;
    }

    public PresenceInfo getUserPresence(Long userId) {
        String userKey = USER_KEY_PREFIX + userId.toString();
        Map<Object, Object> data = redisTemplate.opsForHash().entries(userKey);

        if (data.isEmpty()) {
            PresenceInfo info = new PresenceInfo();
            info.setUserId(userId);
            info.setStatus("OFFLINE");
            info.setActiveSessions(0);
            return info;
        }

        PresenceInfo info = new PresenceInfo();
        info.setUserId(userId);
        info.setUserEmail(getStringValue(data, "email"));
        info.setUserName(getStringValue(data, "name"));
        info.setRole(getStringValue(data, "role"));
        info.setTenantId(getStringValue(data, "tenantId"));
        info.setStatus(getStringValue(data, "status"));
        info.setDevice(getStringValue(data, "lastDevice"));
        info.setBrowser(getStringValue(data, "lastBrowser"));
        info.setOs(getStringValue(data, "lastOs"));
        info.setIp(getStringValue(data, "lastIp"));

        String activeSessionsStr = getStringValue(data, "activeSessions");
        info.setActiveSessions(activeSessionsStr != null ? Integer.parseInt(activeSessionsStr) : 0);

        String connectedAtStr = getStringValue(data, "connectedAt");
        if (connectedAtStr != null) info.setConnectedAt(LocalDateTime.parse(connectedAtStr));

        String disconnectedAtStr = getStringValue(data, "disconnectedAt");
        if (disconnectedAtStr != null) info.setDisconnectedAt(LocalDateTime.parse(disconnectedAtStr));

        String lastSeenAtStr = getStringValue(data, "lastSeenAt");
        if (lastSeenAtStr != null) info.setLastSeenAt(LocalDateTime.parse(lastSeenAtStr));

        // Calculate session duration
        String status = getStringValue(data, "status");
        if ("ONLINE".equals(status) && connectedAtStr != null) {
            // User is online - calculate live duration
            try {
                LocalDateTime connectedAt = LocalDateTime.parse(connectedAtStr);
                long seconds = ChronoUnit.SECONDS.between(connectedAt, LocalDateTime.now());
                info.setSessionDuration(formatDuration(seconds));
                info.setSessionDurationSeconds(seconds);
            } catch (Exception ignored) {}
        } else {
            // User is offline - use stored duration
            String durationStr = getStringValue(data, "lastSessionDuration");
            if (durationStr != null) info.setSessionDuration(durationStr);
            String durationSecondsStr = getStringValue(data, "lastSessionDurationSeconds");
            if (durationSecondsStr != null) {
                try {
                    info.setSessionDurationSeconds(Long.parseLong(durationSecondsStr));
                } catch (NumberFormatException ignored) {}
            }
        }

        return info;
    }

    public List<PresenceInfo> getAllOnlinePresences() {
        Set<Long> onlineIds = getOnlineUserIds();
        List<PresenceInfo> presences = new ArrayList<>();
        for (Long userId : onlineIds) {
            presences.add(getUserPresence(userId));
        }
        return presences;
    }

    /**
     * Get all tracked users (online AND recently offline).
     * Returns users who have presence data in Redis (within 24h TTL).
     */
    public List<PresenceInfo> getAllPresences() {
        Set<Long> onlineIds = getOnlineUserIds();
        List<PresenceInfo> presences = new ArrayList<>();

        // First add all online users
        for (Long userId : onlineIds) {
            presences.add(getUserPresence(userId));
        }

        // Scan for offline user keys - users who disconnected recently
        Set<String> keys = redisTemplate.keys(USER_KEY_PREFIX + "*");
        if (keys != null) {
            for (String key : keys) {
                String userIdStr = key.replace(USER_KEY_PREFIX, "");
                try {
                    Long userId = Long.parseLong(userIdStr);
                    if (!onlineIds.contains(userId)) {
                        PresenceInfo info = getUserPresence(userId);
                        if (info.getUserEmail() != null) {
                            presences.add(info);
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        return presences;
    }

    public boolean isUserOnline(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ONLINE_SET_KEY, userId.toString()));
    }

    @Scheduled(fixedRate = 120000) // Every 2 minutes
    public void cleanupStaleSessions() {
        Set<String> onlineUserIds = redisTemplate.opsForSet().members(ONLINE_SET_KEY);
        if (onlineUserIds == null || onlineUserIds.isEmpty()) return;

        int cleaned = 0;
        for (String userIdStr : new HashSet<>(onlineUserIds)) {
            String userSessionsKey = USER_SESSIONS_PREFIX + userIdStr;
            Set<String> sessionIds = redisTemplate.opsForSet().members(userSessionsKey);

            if (sessionIds == null || sessionIds.isEmpty()) {
                // No sessions left, mark user offline and broadcast event
                markUserOfflineAndBroadcast(userIdStr);
                cleaned++;
                continue;
            }

            // Check each session - remove expired ones
            boolean anyRemoved = false;
            for (String sessionId : sessionIds) {
                String sessionKey = SESSION_KEY_PREFIX + sessionId;
                if (Boolean.FALSE.equals(redisTemplate.hasKey(sessionKey))) {
                    redisTemplate.opsForSet().remove(userSessionsKey, sessionId);
                    // Also clean up reverse mapping
                    redisTemplate.delete(SESSION_USER_PREFIX + sessionId);
                    anyRemoved = true;
                }
            }

            // Re-check after cleanup
            Long remaining = redisTemplate.opsForSet().size(userSessionsKey);
            if (remaining == null || remaining == 0) {
                markUserOfflineAndBroadcast(userIdStr);
                cleaned++;
            } else if (anyRemoved) {
                // Update active sessions count
                String userKey = USER_KEY_PREFIX + userIdStr;
                redisTemplate.opsForHash().put(userKey, "activeSessions", String.valueOf(remaining));
            }
        }

        if (cleaned > 0) {
            log.info("Cleaned up {} stale presence entries, onlineCount now={}", cleaned, getOnlineCount());
        }
    }

    private void markUserOfflineAndBroadcast(String userIdStr) {
        String userKey = USER_KEY_PREFIX + userIdStr;
        LocalDateTime now = LocalDateTime.now();

        // Get user info before updating status
        Object emailObj = redisTemplate.opsForHash().get(userKey, "email");
        String email = emailObj != null ? emailObj.toString() : "unknown";
        Object nameObj = redisTemplate.opsForHash().get(userKey, "name");
        String name = nameObj != null ? nameObj.toString() : email;
        Object roleObj = redisTemplate.opsForHash().get(userKey, "role");
        String role = roleObj != null ? roleObj.toString() : "";
        Object deviceObj = redisTemplate.opsForHash().get(userKey, "lastDevice");
        String device = deviceObj != null ? deviceObj.toString() : "";

        // Calculate session duration
        Object connectedAtObj = redisTemplate.opsForHash().get(userKey, "connectedAt");
        String sessionDuration = "";
        long sessionDurationSeconds = 0;
        if (connectedAtObj != null) {
            try {
                LocalDateTime connectedAt = LocalDateTime.parse(connectedAtObj.toString());
                sessionDurationSeconds = ChronoUnit.SECONDS.between(connectedAt, now);
                sessionDuration = formatDuration(sessionDurationSeconds);
            } catch (Exception ignored) {}
        }

        // Remove from online set
        redisTemplate.opsForSet().remove(ONLINE_SET_KEY, userIdStr);

        // Update user presence in Redis
        redisTemplate.opsForHash().put(userKey, "status", "OFFLINE");
        redisTemplate.opsForHash().put(userKey, "activeSessions", "0");
        redisTemplate.opsForHash().put(userKey, "disconnectedAt", now.toString());
        redisTemplate.opsForHash().put(userKey, "lastSeenAt", now.toString());
        redisTemplate.opsForHash().put(userKey, "lastSessionDuration", sessionDuration);
        redisTemplate.opsForHash().put(userKey, "lastSessionDurationSeconds", String.valueOf(sessionDurationSeconds));
        redisTemplate.expire(userKey, USER_TTL);

        // Broadcast USER_OFFLINE event so frontend updates the count
        try {
            Long userId = Long.parseLong(userIdStr);
            long onlineCount = getOnlineCount();
            PresenceEvent event = new PresenceEvent(
                    PresenceEvent.EventType.USER_OFFLINE, userId, email,
                    name, "OFFLINE", onlineCount);
            event.setDevice(device);
            event.setRole(role);
            event.setDisconnectedAt(now);
            event.setLastSeenAt(now);
            event.setSessionDuration(sessionDuration);
            event.setSessionDurationSeconds(sessionDurationSeconds);
            if (connectedAtObj != null) {
                try {
                    event.setConnectedAt(LocalDateTime.parse(connectedAtObj.toString()));
                } catch (Exception ignored) {}
            }
            messagingTemplate.convertAndSend("/topic/presence", event);
            log.info("Broadcast OFFLINE for stale user: userId={}, email={}, duration={}, onlineCount={}",
                    userIdStr, email, sessionDuration, onlineCount);
        } catch (Exception e) {
            log.warn("Failed to broadcast offline event for userId={}: {}", userIdStr, e.getMessage());
        }
    }

    // ---- Duration formatting ----

    static String formatDuration(long totalSeconds) {
        if (totalSeconds < 0) return "0s";
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0 || hours > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");
        return sb.toString().trim();
    }

    // ---- User-Agent parsing helpers ----

    private static final Pattern BROWSER_PATTERN = Pattern.compile(
            "(Firefox|Chrome|Safari|Opera|Edge|MSIE|Trident)[/\\s]([\\d.]+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern OS_PATTERN = Pattern.compile(
            "(Windows NT [\\d.]+|Mac OS X [\\d_.]+|Linux|Android [\\d.]+|iOS [\\d_.]+|iPhone OS [\\d_.]+)",
            Pattern.CASE_INSENSITIVE);

    static String parseBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        Matcher m = BROWSER_PATTERN.matcher(userAgent);
        if (m.find()) {
            String name = m.group(1);
            // Edge uses "Edg" in newer versions
            if (userAgent.contains("Edg/")) return "Edge";
            if ("Trident".equalsIgnoreCase(name)) return "IE";
            return name;
        }
        if (userAgent.contains("Edg/")) return "Edge";
        return "Unknown";
    }

    static String parseOs(String userAgent) {
        if (userAgent == null) return "Unknown";
        Matcher m = OS_PATTERN.matcher(userAgent);
        if (m.find()) {
            String os = m.group(1);
            if (os.startsWith("Windows NT 10")) return "Windows 10+";
            if (os.startsWith("Windows NT 6.3")) return "Windows 8.1";
            if (os.startsWith("Windows NT 6.1")) return "Windows 7";
            if (os.startsWith("Mac OS X")) return "macOS";
            if (os.startsWith("iPhone OS")) return "iOS";
            return os.replace('_', '.');
        }
        if (userAgent.contains("Linux")) return "Linux";
        return "Unknown";
    }

    private String getStringValue(Map<Object, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
}