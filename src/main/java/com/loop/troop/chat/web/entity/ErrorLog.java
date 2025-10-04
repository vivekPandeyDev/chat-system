package com.loop.troop.chat.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID transactionId;
    private String errorType;      // SERVICE, VALIDATION, UNEXPECTED
    private String errorCode;      // e.g., ACCOUNT_NOT_FOUND, VALIDATION_ERROR
    private String message;        // User-friendly or validation message
    @Column(length = 4000)
    private String detail;         // Dynamic detail / context
    @Column(length = 8000)
    private String stackTrace;     // Full stack trace for developers
    private String path;           // Request URI
    private LocalDateTime occurredAt;

    public ErrorLog() {}
    public ErrorLog(String errorType, String errorCode, String message, String detail, String stackTrace, String path, LocalDateTime occurredAt) {
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.message = message;
        this.detail = detail;
        this.stackTrace = stackTrace;
        this.path = path;
        this.occurredAt = occurredAt;
    }
}
