package com.loop.troop.chat.infrastructure.jpa.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditable {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    // Auditor fields stored separately to avoid colliding with existing FK columns like created_by
    @CreatedBy
    @Column(name = "audit_created_by")
    private String auditCreatedBy;

    @LastModifiedBy
    @Column(name = "audit_last_modified_by")
    private String auditLastModifiedBy;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getAuditCreatedBy() {
        return auditCreatedBy;
    }

    public void setAuditCreatedBy(String auditCreatedBy) {
        this.auditCreatedBy = auditCreatedBy;
    }

    public String getAuditLastModifiedBy() {
        return auditLastModifiedBy;
    }

    public void setAuditLastModifiedBy(String auditLastModifiedBy) {
        this.auditLastModifiedBy = auditLastModifiedBy;
    }
}
