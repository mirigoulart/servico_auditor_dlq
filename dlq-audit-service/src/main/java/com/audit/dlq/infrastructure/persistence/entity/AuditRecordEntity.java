package com.audit.dlq.infrastructure.persistence.entity;

import com.audit.dlq.domain.model.ErrorSeverity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidade JPA para persistência no banco de dados.
 *
 * Separada intencionalmente do modelo de domínio (AuditRecord):
 * mudanças no schema do banco nunca afetam o núcleo do hexágono.
 */
@Entity
@Table(name = "audit_records")
public class AuditRecordEntity {

    @Id
    @Column(name = "error_id", nullable = false, updatable = false)
    private UUID errorId;

    @Column(name = "queue_name", nullable = false)
    private String queueName;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "status", nullable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private ErrorSeverity severity;

    protected AuditRecordEntity() {}

    public AuditRecordEntity(UUID errorId, String queueName, String payload,
                              Instant timestamp, String status, ErrorSeverity severity) {
        this.errorId   = errorId;
        this.queueName = queueName;
        this.payload   = payload;
        this.timestamp = timestamp;
        this.status    = status;
        this.severity  = severity;
    }

    public UUID getErrorId()           { return errorId; }
    public String getQueueName()       { return queueName; }
    public String getPayload()         { return payload; }
    public Instant getTimestamp()      { return timestamp; }
    public String getStatus()          { return status; }
    public ErrorSeverity getSeverity() { return severity; }
}
