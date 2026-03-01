package com.digiSchool.digiSchool.auth.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "login_attempts",
    indexes = {
        @Index(name = "idx_login_attempt_email", columnList = "email"),
        @Index(name = "idx_login_attempt_ip", columnList = "ip_address"),
        @Index(name = "idx_login_attempt_time", columnList = "attempt_time")
    }
)
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(nullable = false)
    private Boolean success;

    @Column(name = "failure_reason")
    private String failureReason;

    @CreationTimestamp
    @Column(name = "attempt_time", updatable = false)
    private LocalDateTime attemptTime;

    public LoginAttempt() {}

    public LoginAttempt(String email, String ipAddress, Boolean success, String failureReason) {
        this.email = email;
        this.ipAddress = ipAddress;
        this.success = success;
        this.failureReason = failureReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }
}
